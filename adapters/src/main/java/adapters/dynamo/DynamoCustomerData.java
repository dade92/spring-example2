package adapters.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public final class DynamoCustomerData {
    private String username;
    private String password;
    private int age;
    private DynamoFavouriteDestinations dynamoFavouriteDestinations;

    public DynamoCustomerData() {
    }

    public DynamoCustomerData(
        String username,
        String password,
        int age,
        DynamoFavouriteDestinations dynamoFavouriteDestinations
    ) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.dynamoFavouriteDestinations = dynamoFavouriteDestinations;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public DynamoFavouriteDestinations getDynamoFavouriteDestinations() {
        return dynamoFavouriteDestinations;
    }

    public void setDynamoFavouriteDestinations(DynamoFavouriteDestinations dynamoFavouriteDestinations) {
        this.dynamoFavouriteDestinations = dynamoFavouriteDestinations;
    }
}
