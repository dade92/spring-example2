package adapters.dynamo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Objects;

@DynamoDbBean
public final class DynamoCustomerData {
    private String username;
    private String password;
    private int age;
    private DynamoPerson person;

    public DynamoCustomerData() {
    }

    public DynamoCustomerData(
        String username,
        String password,
        int age
    ) {
        this.username = username;
        this.password = password;
        this.age = age;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DynamoCustomerData) obj;
        return Objects.equals(this.username, that.username) &&
            Objects.equals(this.password, that.password) &&
            this.age == that.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, age);
    }

    @Override
    public String toString() {
        return "DynamoCustomerData[" +
            "username=" + username + ", " +
            "password=" + password + ", " +
            "age=" + age + ']';
    }

    public DynamoPerson getPerson() {
        return person;
    }

    public void setPerson(DynamoPerson person) {
        this.person = person;
    }
}
