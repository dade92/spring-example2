package adapters.customers.dynamo

import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter {
    fun toCustomer(dynamoCustomer: DynamoCustomer): Customer =
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

    fun toDynamoCustomer(customer: Customer): DynamoCustomer = DynamoCustomer(
        customer.id.value, customer.name.value, DynamoCustomerData(
            customer.name.value,
            "XXX",
            customer.age,
            DynamoFavouriteDestinations(customer.favouriteDestinations.destinations.map { it.city })
        )
    )

}