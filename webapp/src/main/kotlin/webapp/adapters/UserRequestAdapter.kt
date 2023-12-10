package webapp.adapters

import domain.Customer
import domain.UUIDGenerator
import domain.toId
import webapp.InsertCustomerRequest

class UserRequestAdapter(
    private val uuidGenerator: UUIDGenerator
) {
    fun adapt(insertCustomerRequest: InsertCustomerRequest): Customer = Customer(
        uuidGenerator.get().toId(),
        insertCustomerRequest.name,
        insertCustomerRequest.age,
        insertCustomerRequest.favouriteDestinations
    )

}