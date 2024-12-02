package adapters.configuration;

import com.mongodb.ConnectionString;

public class MongoConnectionStringBuilder {

    public ConnectionString build(
        String baseUrl,
        String username,
        String password
    ) {
        return new ConnectionString("mongodb://" + username + ":" + password + "@" + baseUrl + ":27017/?authSource=admin");
    }

}
