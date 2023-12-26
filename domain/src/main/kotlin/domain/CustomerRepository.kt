package domain

import arrow.core.Either

interface CustomerRepository {
    fun insert(customer: Customer): Either<GenericDbError, Unit>
    fun find(name: Name): Either<CustomerNotFoundError, Customer>
    fun findById(id: Id): Either<CustomerNotFoundError, Customer>
    fun addDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun removeDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit>

    fun getAll(): List<Customer>
}