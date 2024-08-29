package adapters.customers

import adapters.dynamo.DynamoCustomer
import adapters.dynamo.DynamoCustomerData
import adapters.dynamo.DynamoFavouriteDestinations
import adapters.dynamo.DynamoPerson
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.JsonProcessingException
import domain.Customer
import domain.Destination
import domain.Error
import domain.Id
import domain.Name
import domain.repository.CustomerRepository
import org.slf4j.LoggerFactory
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException


class DynamoDbCustomersRepository(
    private val customerTable: DynamoDbTable<DynamoCustomer>,
    private val dynamoCustomerAdapter: DynamoCustomerAdapter
) : CustomerRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun insert(customer: Customer): Either<Error, Id> {
        var customerJson: String? = null
        try {
//            customerJson = objectMapper.writeValueAsString()
//            val item: MutableMap<String, AttributeValue> = HashMap()
//            item["ID"] = AttributeValue.builder().s(customer.id.value).build()
//            item["Data"] = AttributeValue.builder().s(customerJson).build()
//
//            val request = PutItemRequest.builder()
//                .tableName(tableName)
//                .item(item)
//                .build()

            customerTable.putItem(customer.toDynamoCustomer())
            return customer.id.right()
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    override fun remove(id: Id): Either<Error, Unit> {
        return Error.GenericError.left()
    }

    override fun find(name: Name): Either<Error, Customer> {
//        val queryConditional = QueryConditional.keyEqualTo(Key.builder().sortValue(name.value).build())
//
//
//        val request = QueryEnhancedRequest.builder()
//            .queryConditional(queryConditional)
//            .build()
//
//        try {
//            val results: Iterator<DynamoCustomer> = customerTable.query(request).items().iterator()
//
//            return dynamoCustomerAdapter.adapt(results.next()).right()
//        } catch (e: DynamoDbException) {
//            System.err.println(e.message)
//            return Error.GenericError.left()
//        }

        val key = Key.builder()
            .partitionValue(name.value)
            .build()

        try {
            return dynamoCustomerAdapter.adapt(customerTable.getItem(key)).right()
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
            return Error.GenericError.left()
        }
    }

    override fun findById(id: Id): Either<Error, Customer> {
//        val key: MutableMap<String, AttributeValue> = HashMap()
//        key["ID"] = AttributeValue.builder().s(id.value).build()
//
//        val request = GetItemRequest.builder()
//            .tableName(tableName)
//            .key(key)
//            .build()
//
//        val returnedItem = dynamoDbClient.getItem(request).item()
//
//        if (returnedItem != null) {
//            val readValue = objectMapper.readValue(returnedItem["Data"]!!.s(), DynamoCustomer::class.java)
//
//            return Customer(
//                readValue.id.toId(),
//                readValue.name.toName(),
//                readValue.age,
//                FavouriteDestinations(emptyList())
//            ).right()

        val key = Key.builder()
            .partitionValue(id.value)
            .build()

        try {
            return dynamoCustomerAdapter.adapt(customerTable.getItem(key)).right()
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
            return Error.GenericError.left()
        }

//        } else {
//            return Error.GenericError.left()
//        }
    }

    override fun addDestination(id: Id, destination: Destination): Either<Error, Unit> {
        return Error.GenericError.left()
    }

    override fun removeDestination(id: Id, destination: Destination): Either<Error, Unit> {
        return Error.GenericError.left()
    }

    override fun updateDestination(
        oldDestination: Destination,
        newDestination: Destination,
        id: Id
    ): Either<Error, Unit> {
        return Error.GenericError.left()
    }

    override fun getAll(): List<Customer> {
        return listOf()
    }

    private fun Customer.toDynamoCustomer(): DynamoCustomer {
        val dynamoCustomer = DynamoCustomer()
        dynamoCustomer.id = this.id.value
        dynamoCustomer.username = this.name.value
        val dynamoCustomerData = DynamoCustomerData(
            this.name.value,
            "XXX",
            this.age,
            DynamoFavouriteDestinations(this.favouriteDestinations.destinations.map { it.city })
        )
        dynamoCustomer.dynamoCustomerData = dynamoCustomerData
        return dynamoCustomer
    }
}