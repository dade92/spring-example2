package domain

import arrow.core.right
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.jmock.AbstractExpectations.returnValue
import org.jmock.Expectations
import org.jmock.auto.Mock
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class DefaultFindCustomerUseCaseTest {

    @RegisterExtension
    @JvmField
    val context = JUnit5Mockery()

    @Mock
    private lateinit var customerRepository: CustomerRepository

    private lateinit var defaultFindCustomerUseCase: DefaultFindCustomerUseCase

    @BeforeEach
    fun setUp() {
        defaultFindCustomerUseCase = DefaultFindCustomerUseCase(customerRepository)
    }

    @Test
    fun `insert success`() {
        context.checking(Expectations().apply {
            oneOf(customerRepository).find("Davide".toName())
            will(returnValue(Customer("1".toId(), "Davide".toName(), 31, FavouriteDestinations(emptyList())).right()))
        })

        assertThat(
            defaultFindCustomerUseCase.findBy("Davide".toName()),
            `is`(Customer("1".toId(), "Davide".toName(), 31, FavouriteDestinations(emptyList())).right())
        )
    }
}