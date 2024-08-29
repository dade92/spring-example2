package adapters.customers.dynamo

import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter {
    fun adapt(dynamoCustomer: DynamoCustomer): Customer =
        Customer(
            dynamoCustomer.ID!!.toId(),
            dynamoCustomer.dynamoCustomerData!!.username!!.toName(),
            dynamoCustomer.dynamoCustomerData!!.age,
            FavouriteDestinations(dynamoCustomer.dynamoCustomerData!!.favouriteDestinations!!.destinations!!.map { city ->
                Destination(
                    city
                )
            })
        )
}