package adapters.customers

import adapters.dynamo.DynamoCustomer
import adapters.dynamo.DynamoCustomerData
import com.fasterxml.jackson.databind.ObjectMapper
import domain.Customer
import domain.FavouriteDestinations
import domain.toId
import domain.toName

class DynamoCustomerAdapter(
    val objectMapper: ObjectMapper
) {
    fun adapt(dynamoCustomer: DynamoCustomer): Customer {
        val value = objectMapper.readValue(dynamoCustomer.data, DynamoCustomerData::class.java)

        return Customer(
            dynamoCustomer.id.toId(),
            value.username.toName(),
            value.age,
            FavouriteDestinations(emptyList())
        )
    }
}