package adapters

import adapters.configuration.AdaptersConfiguration
import adapters.configuration.MongoConfiguration
import arrow.core.left
import arrow.core.right
import domain.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [AdaptersConfiguration::class, MongoConfiguration::class, MongoDBTestContainerConfig::class])
@Testcontainers
class MongoCustomerRepositoryIT {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Test
    fun `insert successfully and remove the customer afterwards`() {
        val customerId = "5".toId()
        val newCustomer = aCustomer(customerId, "John Doe".toName())

        val actualId = customerRepository.insert(newCustomer)

        actualId shouldBe customerId.right()

        customerRepository.findById(customerId) shouldBe newCustomer.right()

        customerRepository.remove(customerId) shouldBe Unit.right()

        customerRepository.findById(customerId) shouldBe CustomerNotFoundError.left()
    }

    @Test
    fun `should retrieve an existing customer`() {
        val alreadyPresentCustomer = Customer(
            id = "1".toId(),
            name = "Davide".toName(),
            age = 31,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("Milan"), Destination("London")))
        )
        customerRepository.find("Davide".toName()) shouldBe alreadyPresentCustomer.right()

        customerRepository.findById("1".toId()) shouldBe alreadyPresentCustomer.right()
    }

    @Test
    fun `add destination to an existing customer`() {
        val newDestination = Destination("Tokyo")
        val id = "2".toId()

        customerRepository.addDestination(id, newDestination)

        customerRepository.findById(id) shouldBe Customer(
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
    }

    @Test
    fun `remove destination from existing customer`() {
        val toBeRemoved = Destination("Milan")
        val id = "4".toId()

        customerRepository.removeDestination(id, toBeRemoved)

        customerRepository.findById(id) shouldBe Customer(
            id = id,
            name = "Sergio".toName(),
            age = 62,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("London")))
        ).right()
    }

    @Test
    fun `update an existing destination`() {
        val id = "3".toId()
        customerRepository.updateDestination(Destination("Milan"), Destination("San Diego"), id)

        customerRepository.findById(id) shouldBe Customer(
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
