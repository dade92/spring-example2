package adapters.customers

import adapters.dynamo.DynamoCustomer
import adapters.dynamo.DynamoCustomerData
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Customer
import domain.Destination
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter(
    private val objectMapper: ObjectMapper
) {
    fun adapt(dynamoCustomer: DynamoCustomer): Customer {
        return Customer(
            dynamoCustomer.id.toId(),
            dynamoCustomer.dynamoCustomerData.username.toName(),
            dynamoCustomer.dynamoCustomerData.age,
            FavouriteDestinations(listOf(Destination(dynamoCustomer.dynamoCustomerData.person.firstName)))
        )
    }
}