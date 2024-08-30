package adapters.customers.dynamo

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.core.JsonProcessingException
import domain.Customer
import domain.Destination
import domain.Error
import domain.FavouriteDestinations
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
            customerTable.putItem(dynamoCustomerAdapter.toDynamoCustomer(customer))
            return customer.id.right()
        } catch (e: JsonProcessingException) {
            return Error.GenericError.left()
        }
    }

    override fun findById(id: Id): Either<Error, Customer> =
        try {
            Key.builder()
                .partitionValue(id.value)
                .build().let { key ->
                    dynamoCustomerAdapter.toCustomer(customerTable.getItem(key)).right()
                }
        } catch (e: DynamoDbException) {
            logger.error("Error retrieving customer for id $id", e)
            Error.CustomerNotFoundError.left()
        }

    override fun find(name: Name): Either<Error, Customer> =
        try {
            val gsiIndex = customerTable.index(USERNAME_GSI)

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
                dynamoCustomerAdapter.toCustomer(result.get()).right()
            } else {
                Error.CustomerNotFoundError.left()
            }
        } catch (e: DynamoDbException) {
            logger.error("Error querying DynamoDB by name: ${e.message}")
            Error.GenericError.left()
        }

    override fun remove(id: Id): Either<Error, Unit> =
        try {
            val key = Key.builder()
                .partitionValue(id.value)
                .build()

            customerTable.deleteItem(key)

            Unit.right()
        } catch (e: DynamoDbException) {
            logger.error("Error removing customer from DynamoDB: ${e.message}")
            Error.GenericError.left()
        }

    override fun updateDestination(
        oldDestination: Destination,
        newDestination: Destination,
        id: Id
    ): Either<Error, Unit> =
        findById(id).fold(
            {
                logger.error("Customer with id $id not found", it)
                Error.CustomerNotFoundError.left()
            },
            { customer ->
                val filteredDestinations = customer
                    .favouriteDestinations
                    .destinations
                    .filter { it.city != oldDestination.city }
                    .toMutableList()

                filteredDestinations.add(newDestination)

                customerTable.putItem(
                    dynamoCustomerAdapter.toDynamoCustomer(
                        customer.copy(
                            favouriteDestinations = FavouriteDestinations(filteredDestinations)
                        )
                    )
                )

                Unit.right()
            }
        )

    override fun addDestination(id: Id, destination: Destination): Either<Error, Unit> =
        findById(id).fold(
            {
                logger.error("Customer with id $id not found", it)
                Error.CustomerNotFoundError.left()
            },
            { customer ->
                val toBeChanged = customer
                    .favouriteDestinations
                    .destinations
                    .toMutableList()

                toBeChanged.add(destination)

                customerTable.putItem(
                    dynamoCustomerAdapter.toDynamoCustomer(
                        customer.copy(
                            favouriteDestinations = FavouriteDestinations(toBeChanged)
                        )
                    )
                )

                Unit.right()
            }
        )

    override fun removeDestination(id: Id, destination: Destination): Either<Error, Unit> =
        findById(id).fold(
            {
                logger.error("Customer with id $id not found", it)
                Error.CustomerNotFoundError.left()
            },
            { customer ->
                val filteredDestinations = customer
                    .favouriteDestinations
                    .destinations
                    .filter { it.city != destination.city }

                customerTable.putItem(
                    dynamoCustomerAdapter.toDynamoCustomer(
                        customer.copy(
                            favouriteDestinations = FavouriteDestinations(filteredDestinations)
                        )
                    )
                )

                Unit.right()
            }
        )

    companion object {
        private val USERNAME_GSI = "UsernameIndex"
    }
}