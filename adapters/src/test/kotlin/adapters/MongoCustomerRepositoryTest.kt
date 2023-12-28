package adapters

import adapters.configuration.AdaptersConfiguration
import adapters.configuration.MongoConfiguration
import arrow.core.left
import arrow.core.right
import domain.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import org.hamcrest.MatcherAssert.assertThat


@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = [AdaptersConfiguration::class, MongoConfiguration::class, MongoDBTestContainerConfig::class])
class MongoCustomerRepositoryIntegrationTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `insert successfully and remove the customer afterwards`() {
        val customerId = "4".toId()
        val newCustomer = aCustomer(customerId, "John Doe".toName())

        val result = customerRepository.insert(newCustomer)

        assertThat(result, `is`(customerId.right()))

        assertThat(customerRepository.findById(customerId), `is`(newCustomer.right()))

        assertThat(customerRepository.remove(customerId), `is`(Unit.right()))

        assertThat(customerRepository.findById(customerId), `is`(CustomerNotFoundError.left()))
    }

    @Test
    fun `should retrieve an existing customer`() {
        val alreadyPresentCustomer = Customer(
            id = "1".toId(),
            name = "Davide".toName(),
            age = 31,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("Milan"), Destination("London")))
        )
        assertThat(customerRepository.find("Davide".toName()), `is`(alreadyPresentCustomer.right()))

        assertThat(customerRepository.findById("1".toId()), `is`(alreadyPresentCustomer.right()))
    }

    @Test
    fun `add destination to an existing customer`() {
        val newDestination = Destination("Tokyo")
        val id = "2".toId()

        customerRepository.addDestination(id, newDestination)

        assertThat(
            customerRepository.findById(id), `is`(
                Customer(
                    id = id,
                    name = "Sergio".toName(),
                    age = 62,
                    favouriteDestinations = FavouriteDestinations(
                        listOf(
                            Destination("Milan"),
                            Destination("London"),
                            newDestination
                        )
                    )
                ).right()
            )
        )
    }

    @Test
    fun `update an existing destination`() {
        val id = "3".toId()
        customerRepository.updateDestination(Destination("Milan"), Destination("San Diego"), id)

        assertThat(
            customerRepository.findById(id), `is`(
                Customer(
                    id = id,
                    name = "Paola".toName(),
                    age = 57,
                    favouriteDestinations = FavouriteDestinations(
                        listOf(
                            Destination("San Diego"),
                            Destination("London"),
                        )
                    )
                ).right()
            )
        )
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
