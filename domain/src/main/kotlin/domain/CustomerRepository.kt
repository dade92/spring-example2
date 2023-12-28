package domain

import arrow.core.Either

interface CustomerRepository {
    fun insert(customer: Customer): Either<GenericDbError, Id>
    fun remove(id: Id): Either<GenericDbError, Unit>
    fun find(name: Name): Either<CustomerNotFoundError, Customer>
    fun findById(id: Id): Either<CustomerNotFoundError, Customer>
    fun addDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun removeDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun updateDestination(oldDestination: Destination, newDestination: Destination, id: Id): Either<CustomerNotFoundError, Unit>

    fun getAll(): List<Customer>
}