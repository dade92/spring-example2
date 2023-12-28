package adapters

import adapters.configuration.AdaptersConfiguration
import adapters.configuration.MongoConfiguration
import arrow.core.right
import domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = [AdaptersConfiguration::class, MongoConfiguration::class, MongoDBTestContainerConfig::class])
class MongoCustomerRepositoryIntegrationTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    val customer = aCustomer("3".toId(), "John Doe".toName())

    @Test
    fun `insert should return customer id on success`() {
        val result = customerRepository.insert(customer)

        assertThat(result).isEqualTo("3".toId().right())
    }

    @Test
    fun `should retrieve an existing customer`() {
        val alreadyPresentCustomer = Customer(
            id = "1".toId(),
            name = "Davide".toName(),
            age = 31,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("Milan"), Destination("London")))
        )
        assertThat(customerRepository.find("Davide".toName())).isEqualTo(alreadyPresentCustomer.right())

        assertThat(customerRepository.findById("1".toId())).isEqualTo(alreadyPresentCustomer.right())
    }

    private fun aCustomer(id: Id, name: Name): Customer {
        return Customer(
            name = name,
            age = 30,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("City1"))),
            id = id
        )
    }

}
