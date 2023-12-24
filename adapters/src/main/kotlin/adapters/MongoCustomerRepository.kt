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
import org.springframework.data.mongodb.core.query.Update.update
import java.util.*

private val staticDestinations = listOf(
    Destination("Sidney"), Destination("Tokyo")
)

private val COLLECTION_NAME = "mongocustomer"

class MongoCustomerRepository(
    private val mongoTemplate: MongoTemplate
) : CustomerRepository {

    private val logger = LoggerFactory.getLogger(MongoCustomerRepository::class.java)

    override fun insert(customer: Customer) {
        mongoTemplate.insert(MongoCustomer.fromDomain(customer), COLLECTION_NAME)
    }

    override fun find(name: String): Either<CustomerNotFoundError, Customer> {
        val query = query(Criteria.where("name").`is`(name))
        return try {
            mongoTemplate.find(query, MongoCustomer::class.java, COLLECTION_NAME)[0].toDomain().right()
        } catch (e: Exception) {
            logger.warn("Unable to find customer because of ", e)
            CustomerNotFoundError.left()
        }
    }

    override fun findById(id: Id): Either<CustomerNotFoundError, Customer> {
        return try {
            mongoTemplate.findById(id.value, MongoCustomer::class.java, COLLECTION_NAME)?.toDomain()?.right()
                ?: CustomerNotFoundError.left()
        } catch (e: Exception) {
            CustomerNotFoundError.left()
        }
    }

    override fun update(name: String): Either<CustomerNotFoundError, Unit> =
        try {
            val query = query(Criteria.where("name").`is`(name))
//            val update = update(
//                "favouriteDestinations",
//                FavouriteDestinations(listOf(Destination("Sidney"), Destination("London")))
//            )
            val update = Update().push("favouriteDestinations.destinations", Destination("San Francisco"))

            mongoTemplate.updateFirst(
                query, update, MongoCustomer::class.java, COLLECTION_NAME
            )
            Unit.right()
        } catch (e: Exception) {
            logger.warn("Can't update customer because of ", e)
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
    val favouriteDestinations: FavouriteDestinations?
) {
    fun toDomain(): Customer = Customer(
        name = name,
        age = age ?: 0,
        favouriteDestinations = favouriteDestinations ?: FavouriteDestinations(emptyList()),
        id = id.toId()
    )

    companion object {
        fun fromDomain(customer: Customer) = MongoCustomer(
            name = customer.name,
            age = customer.age,
            favouriteDestinations = customer.favouriteDestinations,
            id = UUID.randomUUID().toString()
        )
    }
}