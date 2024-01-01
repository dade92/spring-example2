package adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import domain.*
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

private val COLLECTION_NAME = "mongocustomer"

class MongoCustomerRepository(
    private val mongoTemplate: MongoTemplate,
    private val timeProvider: TimeProvider
) : CustomerRepository {

    private val logger = LoggerFactory.getLogger(MongoCustomerRepository::class.java)

    override fun insert(customer: Customer): Either<GenericDbError, Id> =
        try {
            mongoTemplate.insert(MongoCustomer.fromDomain(customer, timeProvider.now()), COLLECTION_NAME)
            customer.id.right()
        } catch (e: Exception) {
            logger.error("Error while inserting customer, due to ", e)
            GenericDbError.left()
        }

    override fun remove(id: Id): Either<GenericDbError, Unit> =
        try {
            val query = query(Criteria.where("_id").`is`(id.value))
            mongoTemplate.remove(query, COLLECTION_NAME)
            Unit.right()
        } catch (e: Exception) {
            logger.error("Error while removing customer with id ${id.value} due to ", e)
            GenericDbError.left()
        }

    override fun find(name: Name): Either<CustomerNotFoundError, Customer> =
        try {
            val query = query(Criteria.where("name").`is`(name.value))
            mongoTemplate.find(query, MongoCustomer::class.java, COLLECTION_NAME)[0].toDomain().right()
        } catch (e: Exception) {
            logger.warn("Unable to find customer because of ", e)
            CustomerNotFoundError.left()
        }

    override fun findById(id: Id): Either<CustomerNotFoundError, Customer> =
        try {
            mongoTemplate.findById(id.value, MongoCustomer::class.java, COLLECTION_NAME)?.toDomain()?.right()
                ?: CustomerNotFoundError.left()
        } catch (e: Exception) {
            logger.warn("Unable to find customer because of ", e)
            CustomerNotFoundError.left()
        }

    override fun addDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit> =
        try {
            val query = query(Criteria.where("_id").`is`(id.value))
//            val update = update(
//                "favouriteDestinations",
//                FavouriteDestinations(listOf(Destination("Sidney"), Destination("London")))
//            )
            val update = Update().push("favouriteDestinations.destinations", destination)

            mongoTemplate.updateFirst(
                query, update, MongoCustomer::class.java, COLLECTION_NAME
            )
            Unit.right()
        } catch (e: Exception) {
            logger.warn("Can't update customer because of ", e)
            CustomerNotFoundError.left()
        }

    override fun removeDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit> =
        try {
            val query = query(Criteria.where("_id").`is`(id.value))

            val update = Update().pull("favouriteDestinations.destinations", destination)

            mongoTemplate.updateFirst(
                query, update, MongoCustomer::class.java, COLLECTION_NAME
            )
            Unit.right()
        } catch (e: Exception) {
            logger.warn("Can't update customer because of ", e)
            CustomerNotFoundError.left()
        }

    override fun updateDestination(
        oldDestination: Destination,
        newDestination: Destination,
        id: Id
    ): Either<CustomerNotFoundError, Unit> =
        try {
            val query = Query(
                Criteria.where("_id").`is`(id.value)
                    .and("favouriteDestinations.destinations.city").`is`(oldDestination.city)
            )

            val update = Update().set("favouriteDestinations.destinations.$.city", newDestination.city)

            mongoTemplate.updateFirst(query, update, COLLECTION_NAME)
            Unit.right()
        } catch (e: Exception) {
            logger.error("Error updating destination for customer ${id.value} due to ", e)
            CustomerNotFoundError.left()
        }

    override fun getAll(): List<Customer> {
        val query = Query()
        return mongoTemplate.find(query, MongoCustomer::class.java, COLLECTION_NAME).map { it.toDomain() }
    }

}

data class MongoCustomer(
    val id: String,
    val name: String,
    val age: Int?,
    val favouriteDestinations: FavouriteDestinations?,
    val creationDate: String?
) {
    fun toDomain(): Customer = Customer(
        name = name.toName(),
        age = age ?: 0,
        favouriteDestinations = favouriteDestinations ?: FavouriteDestinations(emptyList()),
        id = id.toId()
    )

    companion object {
        fun fromDomain(customer: Customer, now: LocalDateTime) = MongoCustomer(
            name = customer.name.value,
            age = customer.age,
            favouriteDestinations = customer.favouriteDestinations,
            id = customer.id.value,
            creationDate = now.truncatedTo(ChronoUnit.SECONDS).toString()
        )
    }
}