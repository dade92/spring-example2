package adapters.customers

import adapters.dynamo.DynamoCustomer
import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter {
    fun adapt(dynamoCustomer: DynamoCustomer): Customer {
        return Customer(
            dynamoCustomer.id.toId(),
            dynamoCustomer.dynamoCustomerData.username.toName(),
            dynamoCustomer.dynamoCustomerData.age,
            FavouriteDestinations(listOf(Destination(dynamoCustomer.dynamoCustomerData.person.firstName)))
        )
    }
}