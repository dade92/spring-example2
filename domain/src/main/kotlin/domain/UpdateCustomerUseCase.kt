package domain

import arrow.core.Either

interface UpdateCustomerUseCase {
    fun update(name: String): Either<CustomerNotFoundError, Unit>
}

class DefaultUpdateCustomerUseCase(
    private val customerRepository: CustomerRepository
) : UpdateCustomerUseCase {
    override fun update(name: String): Either<CustomerNotFoundError, Unit> = customerRepository.update(name)
}