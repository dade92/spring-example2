package adapters.configuration;

import com.mongodb.ConnectionString;

public class MongoConnectionStringBuilder {

    public ConnectionString build(
        String baseUrl,
        String username,
        String password,
        String port
    ) {
        return new ConnectionString("mongodb://" + username + ":" + password + "@" + baseUrl + ":" + port + "/?authSource=admin");
    }

}
