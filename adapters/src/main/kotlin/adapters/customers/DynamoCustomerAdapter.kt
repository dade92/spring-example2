package adapters.customers

import adapters.dynamo.DynamoCustomer
import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter {
    fun adapt(dynamoCustomer: DynamoCustomer): Customer =
        Customer(
            dynamoCustomer.id.toId(),
            dynamoCustomer.dynamoCustomerData.username.toName(),
            dynamoCustomer.dynamoCustomerData.age,
            FavouriteDestinations(dynamoCustomer.dynamoCustomerData.dynamoFavouriteDestinations.destinations.map { city ->
                Destination(
                    city
                )
            })
        )
}