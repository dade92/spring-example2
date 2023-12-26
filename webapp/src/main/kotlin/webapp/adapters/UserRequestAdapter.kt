package webapp.adapters

import domain.Customer
import domain.UUIDGenerator
import domain.toId
import domain.toName
import webapp.InsertCustomerRequest

class UserRequestAdapter(
    private val uuidGenerator: UUIDGenerator
) {
    fun adapt(insertCustomerRequest: InsertCustomerRequest): Customer = Customer(
        uuidGenerator.get().toId(),
        insertCustomerRequest.name.toName(),
        insertCustomerRequest.age,
        insertCustomerRequest.favouriteDestinations
    )

}