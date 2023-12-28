package domain

import arrow.core.Either

interface UpdateCustomerUseCase {
    fun addDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun removeDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit>
    fun updateDestination(id: Id, oldDestination: Destination, newDestination: Destination): Either<CustomerNotFoundError, Unit>
}

class DefaultUpdateCustomerUseCase(
    private val customerRepository: CustomerRepository
) : UpdateCustomerUseCase {
    override fun addDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit> =
        customerRepository.addDestination(id, destination)

    override fun removeDestination(id: Id, destination: Destination): Either<CustomerNotFoundError, Unit> =
        customerRepository.removeDestination(id, destination)

    override fun updateDestination(
        id: Id,
        oldDestination: Destination,
        newDestination: Destination
    ): Either<CustomerNotFoundError, Unit> = customerRepository.updateDestination(oldDestination, newDestination, id)
}