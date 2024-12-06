package adapters.configuration;

import com.mongodb.ConnectionString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoConnectionStringBuilderTest {

    private final MongoConnectionStringBuilder builder = new MongoConnectionStringBuilder();

    @Test
    void build() {
        ConnectionString actual = builder.build("localhost", "name", "password", "666");
        ConnectionString expected = new ConnectionString("mongodb://name:password@localhost:666/?authSource=admin");

        assertEquals(expected, actual);
    }
}