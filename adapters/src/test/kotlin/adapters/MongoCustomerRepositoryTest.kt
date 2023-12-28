package adapters

import adapters.configuration.AdaptersConfiguration
import adapters.configuration.MongoConfiguration
import arrow.core.left
import arrow.core.right
import domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
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


    @Test
    fun `insert should return customer id on success`() {
        // Arrange
        val customer = createSampleCustomer()

        // Act
        val result = customerRepository.insert(customer)

        // Assert
        assertThat(result).isEqualTo(customer.id.right())
    }

    private fun createSampleCustomer(): Customer {
        return Customer(
            name = Name("John Doe"),
            age = 30,
            favouriteDestinations = FavouriteDestinations(listOf(Destination("City1"))),
            id = Id("123")
        )
    }
}
