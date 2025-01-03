package domain.repository

import arrow.core.Either
import domain.*

interface CustomerRepository {
    fun insert(customer: Customer): Either<Error, Id>
    fun remove(id: Id): Either<Error, Unit>
    fun findAll(name: Name): Either<Error, List<Customer>>
    fun findById(id: Id): Either<Error, Customer>
    fun addDestination(id: Id, destination: Destination): Either<Error, Unit>
    fun removeDestination(id: Id, destination: Destination): Either<Error, Unit>
    fun updateDestination(oldDestination: Destination, newDestination: Destination, id: Id): Either<Error, Unit>
}