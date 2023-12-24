package domain

import arrow.core.Either

interface CustomerRepository {
    fun insert(customer: Customer)
    fun find(name: String): Either<CustomerNotFoundError, Customer>
    fun findById(id: Id): Either<CustomerNotFoundError, Customer>
    fun update(name: String): Either<CustomerNotFoundError, Unit>

    fun getAll(): List<Customer>
}