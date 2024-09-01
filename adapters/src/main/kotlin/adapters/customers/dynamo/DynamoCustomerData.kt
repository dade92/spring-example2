package adapters.customers.dynamo

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

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
