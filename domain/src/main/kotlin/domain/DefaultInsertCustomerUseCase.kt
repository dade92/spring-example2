package domain

import arrow.core.Either

interface InsertCustomerUseCase {
    fun insert(customer: Customer): Either<GenericDbError, Id>
    fun remove(id: Id): Either<GenericDbError, Unit>
}

class DefaultInsertCustomerUseCase(
    private val customerRepository: CustomerRepository
) : InsertCustomerUseCase {

    override fun insert(customer: Customer): Either<GenericDbError, Id> = customerRepository.insert(customer)
    override fun remove(id: Id): Either<GenericDbError, Unit> = customerRepository.remove(id)
}