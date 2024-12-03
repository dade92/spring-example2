package adapters.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mongo")
public class MongoProperties {

    public String host;
    public String username;
    public String password;
    public String port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
