package domain.usecases

import arrow.core.Either
import domain.Customer
import domain.Error
import domain.Id
import domain.Name
import domain.repository.CustomerRepository

interface FindCustomerUseCase {
    fun findById(id: Id): Either<Error, Customer>
    fun findAll(name: Name): Either<Error, List<Customer>>
}

class DefaultFindCustomerUseCase(
    private val customerRepository: CustomerRepository
) : FindCustomerUseCase {
    override fun findById(id: Id): Either<Error, Customer> = customerRepository.findById(id)

    override fun findAll(name: Name): Either<Error, List<Customer>> = customerRepository.findAll(name)

}
