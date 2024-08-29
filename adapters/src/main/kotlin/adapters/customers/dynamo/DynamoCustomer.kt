package adapters.customers.dynamo

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
class DynamoCustomer {
    @get:DynamoDbPartitionKey
    var ID: String? = null
    var username: String? = null
    var dynamoCustomerData: DynamoCustomerData? = null
}
