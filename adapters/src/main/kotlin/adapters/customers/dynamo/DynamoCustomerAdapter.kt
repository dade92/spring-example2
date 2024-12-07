package adapters.customers.dynamo

import domain.*
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey

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
        customer.id.value,
        customer.name.value,
        DynamoCustomerData(
            customer.name.value,
            "XXX",
            customer.age,
            DynamoFavouriteDestinations(customer.favouriteDestinations.destinations.map { it.city })
        )
    )
}

@DynamoDbBean
class DynamoCustomer {
    @get:DynamoDbPartitionKey
    var ID: String? = null

    @get:DynamoDbSecondaryPartitionKey(indexNames = ["UsernameIndex"])
    var username: String? = null
    var dynamoCustomerData: DynamoCustomerData? = null

    constructor()

    constructor(ID: String, username: String, dynamoCustomerData: DynamoCustomerData) {
        this.ID = ID
        this.username = username
        this.dynamoCustomerData = dynamoCustomerData
    }
}

@DynamoDbBean
class DynamoCustomerData {
    var username: String? = null
    var password: String? = null
    var age: Int = 0
    var favouriteDestinations: DynamoFavouriteDestinations? = null

    constructor()

    constructor(
        username: String?,
        password: String?,
        age: Int,
        favouriteDestinations: DynamoFavouriteDestinations?
    ) {
        this.username = username
        this.password = password
        this.age = age
        this.favouriteDestinations = favouriteDestinations
    }
}

@DynamoDbBean
class DynamoFavouriteDestinations {
    var destinations: List<String>? = null

    constructor()

    constructor(destinations: List<String>?) {
        this.destinations = destinations
    }
}