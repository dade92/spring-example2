package adapters

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import domain.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

private val COLLECTION_NAME = "mongocustomer"

class MongoCustomerRepository(
    private val mongoTemplate: MongoTemplate
) : CustomerRepository {

    override fun insert(customer: Customer) {
        mongoTemplate.insert(MongoCustomer.fromDomain(customer), COLLECTION_NAME)
    }

    override fun find(name: String): Either<CustomerNotFoundError, Customer> {
        val query = Query()
        query.addCriteria(Criteria.where("name").`is`(name))
        return try {
            mongoTemplate.find(query, MongoCustomer::class.java, COLLECTION_NAME)[0].toDomain().right()
        } catch (e: Exception) {
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

    override fun getAll(): List<Customer> {
        val query = Query()
        return mongoTemplate.find(query, MongoCustomer::class.java, COLLECTION_NAME).map { it.toDomain() }
    }

}

data class MongoCustomer(
    val name: String,
    val age: Int?,
    val favouriteDestinations: FavouriteDestinations?
) {
    fun toDomain(): Customer = Customer(
        name = name,
        age = age ?: 0,
        favouriteDestinations = favouriteDestinations ?: FavouriteDestinations(emptyList())
    )

    companion object {
        fun fromDomain(customer: Customer) = MongoCustomer(
            name = customer.name,
            age = customer.age,
            favouriteDestinations = customer.favouriteDestinations
        )
    }
}