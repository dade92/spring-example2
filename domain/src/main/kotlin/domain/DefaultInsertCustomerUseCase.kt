package domain

import arrow.core.Either

interface InsertCustomerUseCase {
    fun insert(customer: Customer): Either<Error, Id>
    fun remove(id: Id): Either<Error, Unit>
}

class DefaultInsertCustomerUseCase(
    private val customerRepository: CustomerRepository
) : InsertCustomerUseCase {

    override fun insert(customer: Customer): Either<Error, Id> = customerRepository.insert(customer)
    override fun remove(id: Id): Either<Error, Unit> = customerRepository.remove(id)
}