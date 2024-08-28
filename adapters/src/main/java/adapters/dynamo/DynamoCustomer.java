package adapters.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DynamoCustomer {
    private String ID;
    private String username;
    private String data;
    private DynamoCustomerData dynamoCustomerData;

    @DynamoDbPartitionKey
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DynamoCustomerData getDynamoCustomerData() {
        return dynamoCustomerData;
    }

    public void setDynamoCustomerData(DynamoCustomerData dynamoCustomerData) {
        this.dynamoCustomerData = dynamoCustomerData;
    }
}
