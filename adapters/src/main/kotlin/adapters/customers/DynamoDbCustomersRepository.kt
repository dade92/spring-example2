package adapters.customers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Customer
import domain.Destination
import domain.Error
import domain.Id
import domain.Name
import domain.repository.CustomerRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDbCustomersRepository(
    private val dynamoDbClient: DynamoDbClient,
    private val tableName: String,
    private val objectMapper: ObjectMapper
) : CustomerRepository {
    override fun insert(customer: Customer): Either<Error, Id> {
        var customerJson: String? = null
        try {
            customerJson = objectMapper.writeValueAsString(customer)
            val item: MutableMap<String, AttributeValue> = HashMap()
            item["ID"] = AttributeValue.builder().s(customer.id.value).build()
            item["Data"] = AttributeValue.builder().s(customerJson).build()

            val request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build()

            dynamoDbClient.putItem(request)
            return customer.id.right()
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    override fun remove(id: Id): Either<Error, Unit> {
        return Error.GenericError.left()
    }

    override fun find(name: Name): Either<Error, Customer> {
        return Error.GenericError.left()
    }

    override fun findById(id: Id): Either<Error, Customer> {
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
}
