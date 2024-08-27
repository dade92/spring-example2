package adapters.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public record DynamoCustomerData(
    String username,
    String password,
    int age
) {
}
