package adapters.customers.dynamo

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey

@DynamoDbBean
class DynamoCustomer {
    @get:DynamoDbPartitionKey
    var ID: String? = null
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["UsernameIndex"])
    var username: String? = null
    var dynamoCustomerData: DynamoCustomerData? = null
}
