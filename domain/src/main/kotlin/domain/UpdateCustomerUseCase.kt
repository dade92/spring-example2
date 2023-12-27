package domain

import arrow.core.Either

interface UpdateCustomerUseCase {
    fun addDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun removeDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun updateDestination(name: String, oldDestination: Destination, newDestination: Destination): Either<CustomerNotFoundError, Unit>
}

class DefaultUpdateCustomerUseCase(
    private val customerRepository: CustomerRepository
) : UpdateCustomerUseCase {
    override fun addDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit> =
        customerRepository.addDestination(name, destination)

    override fun removeDestination(name: String, destination: Destination): Either<CustomerNotFoundError, Unit> =
        customerRepository.removeDestination(name, destination)

    override fun updateDestination(
        name: String,
        oldDestination: Destination,
        newDestination: Destination
    ): Either<CustomerNotFoundError, Unit> = customerRepository.updateDestination(oldDestination, newDestination, name)
}