package adapters.customers.dynamo

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
class DynamoFavouriteDestinations {
    var destinations: List<String>? = null

    constructor()

    constructor(destinations: List<String>?) {
        this.destinations = destinations
    }
}
