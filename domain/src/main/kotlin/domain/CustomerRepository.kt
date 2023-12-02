package domain

import arrow.core.Either

interface CustomerRepository {
    fun insert(customer: Customer)
    fun find(name: String): Either<CustomerNotFoundError, Customer>

    fun getAll(): List<Customer>
}