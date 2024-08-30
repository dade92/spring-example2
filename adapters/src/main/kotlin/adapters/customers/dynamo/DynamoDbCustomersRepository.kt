package adapters.customers.dynamo

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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException


class DynamoDbCustomersRepository(
    private val customerTable: DynamoDbTable<DynamoCustomer>,
    private val dynamoCustomerAdapter: DynamoCustomerAdapter
) : CustomerRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun insert(customer: Customer): Either<Error, Id> {
        try {
            customerTable.putItem(customer.toDynamoCustomer())
            return customer.id.right()
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    override fun findById(id: Id): Either<Error, Customer> {
        val key = Key.builder()
            .partitionValue(id.value)
            .build()

        try {
            return dynamoCustomerAdapter.adapt(customerTable.getItem(key)).right()
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
            return Error.GenericError.left()
        }
    }

    override fun find(name: Name): Either<Error, Customer> =
        try {
            val gsiIndex = customerTable.index("UsernameIndex")

            val queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                    .partitionValue(name.value)
                    .build()
            )

            val queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build()

            val result = gsiIndex.query(queryRequest)
                .stream()
                .flatMap { page -> page.items().stream() }
                .findFirst()

            if (result.isPresent) {
                dynamoCustomerAdapter.adapt(result.get()).right()
            } else {
                Error.GenericError.left()
            }
        } catch (e: DynamoDbException) {
            logger.error("Error querying DynamoDB by name: ${e.message}")
            Error.GenericError.left()
        }

    override fun remove(id: Id): Either<Error, Unit> {
        return Error.GenericError.left()
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
        dynamoCustomer.ID = this.id.value
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