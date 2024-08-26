package adapters.customers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Customer
import domain.Destination
import domain.Error
import domain.FavouriteDestinations
import domain.Id
import domain.Name
import domain.repository.CustomerRepository
import domain.toId
import domain.toName
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import kotlin.math.log


class DynamoDbCustomersRepository(
    private val dynamoDbClient: DynamoDbClient,
    private val tableName: String,
    private val objectMapper: ObjectMapper
) : CustomerRepository {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun insert(customer: Customer): Either<Error, Id> {
        var customerJson: String? = null
        try {
            customerJson = objectMapper.writeValueAsString(customer.toDynamoCustomer())
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
        val key: MutableMap<String, AttributeValue> = HashMap()
        key["ID"] = AttributeValue.builder().s(id.value).build()

        val request = GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build()

        val returnedItem = dynamoDbClient.getItem(request).item()

        if (returnedItem != null) {
            val readValue = objectMapper.readValue(returnedItem["Data"]!!.s(), DynamoCustomer::class.java)

            return Customer(
                readValue.id.toId(),
                readValue.name.toName(),
                readValue.age,
                FavouriteDestinations(emptyList())
            ).right()
        } else {
            return Error.GenericError.left()
        }
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

private fun Customer.toDynamoCustomer(): DynamoCustomer = DynamoCustomer(
    this.id.value,
    this.name.value,
    this.age
)

data class DynamoCustomer(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("age")
    val age: Int
)