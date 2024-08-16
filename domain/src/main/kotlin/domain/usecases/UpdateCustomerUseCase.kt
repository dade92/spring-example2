package domain.usecases

import arrow.core.Either
import domain.repository.CustomerRepository
import domain.Destination
import domain.Error
import domain.Id

interface UpdateCustomerUseCase {
    fun addDestination(id: Id, destination: Destination): Either<Error, Unit>
    fun removeDestination(id: Id, destination: Destination): Either<Error, Unit>
    fun updateDestination(id: Id, oldDestination: Destination, newDestination: Destination): Either<Error, Unit>
}

class DefaultUpdateCustomerUseCase(
    private val customerRepository: CustomerRepository
) : UpdateCustomerUseCase {
    override fun addDestination(id: Id, destination: Destination): Either<Error, Unit> =
        customerRepository.addDestination(id, destination)

    override fun removeDestination(id: Id, destination: Destination): Either<Error, Unit> =
        customerRepository.removeDestination(id, destination)

    override fun updateDestination(
        id: Id,
        oldDestination: Destination,
        newDestination: Destination
    ): Either<Error, Unit> = customerRepository.updateDestination(oldDestination, newDestination, id)
}