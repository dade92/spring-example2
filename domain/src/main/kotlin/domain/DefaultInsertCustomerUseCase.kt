package domain

import arrow.core.Either

interface InsertCustomerUseCase {
    fun insert(customer: Customer): Either<GenericDbError, Id>
}

class DefaultInsertCustomerUseCase(
    private val customerRepository: CustomerRepository
) : InsertCustomerUseCase {

    override fun insert(customer: Customer): Either<GenericDbError, Id> = customerRepository.insert(customer)
}