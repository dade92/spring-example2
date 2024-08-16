package domain

import arrow.core.Either

interface FindCustomerUseCase {
    fun findBy(name: Name): Either<Error, Customer>
    fun findById(id: Id): Either<Error, Customer>
    fun getAll(): List<Customer>
}

class DefaultFindCustomerUseCase(
    private val customerRepository: CustomerRepository
) : FindCustomerUseCase {
    override fun findBy(name: Name): Either<Error, Customer> = customerRepository.find(name)

    override fun findById(id: Id): Either<Error, Customer> = customerRepository.findById(id)
    override fun getAll(): List<Customer> {
        return customerRepository.getAll()
    }

}
