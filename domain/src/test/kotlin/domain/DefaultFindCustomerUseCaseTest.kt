package domain

import arrow.core.right
import domain.repository.CustomerRepository
import domain.usecases.DefaultFindCustomerUseCase
import org.jmock.AbstractExpectations.returnValue
import org.jmock.Expectations
import org.jmock.auto.Mock
import org.jmock.junit5.JUnit5Mockery
import org.junit.jupiter.api.Assertions.assertEquals
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
        val expectedResult = listOf(
            Customer(
                "1".toId(),
                "Davide".toName(),
                31,
                FavouriteDestinations(emptyList())
            )
        ).right()

        context.checking(Expectations().apply {
            oneOf(customerRepository).findAll("Davide".toName())
            will(
                returnValue(
                    expectedResult
                )
            )
        })

        assertEquals(
            expectedResult,
            defaultFindCustomerUseCase.findAll("Davide".toName())
        )
    }
}