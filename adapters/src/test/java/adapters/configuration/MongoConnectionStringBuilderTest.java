package adapters.configuration;

import com.mongodb.ConnectionString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MongoConnectionStringBuilderTest {

    private final MongoConnectionStringBuilder builder = new MongoConnectionStringBuilder();

    @Test
    void build() {
        ConnectionString actual = builder.build("localhost", "name", "password", "666");
        ConnectionString expected = new ConnectionString("mongodb://name:password@localhost:666/?authSource=admin");

        Assertions.assertEquals(expected, actual);
    }
}