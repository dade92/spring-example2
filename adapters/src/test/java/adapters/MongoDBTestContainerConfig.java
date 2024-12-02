package adapters;

import adapters.customers.mongo.MongoDBCustomerRepository;
import domain.repository.CustomerRepository;
import domain.utils.DefaultTimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.utility.MountableFile.forClasspathResource;

@Configuration
public class MongoDBTestContainerConfig {

    @Bean
    public CustomerRepository customerRepository(MongoTemplate mongoTemplate) {
        return new MongoDBCustomerRepository(mongoTemplate, new DefaultTimeProvider());
    }

    @Container
    public static final GenericContainer<?> mongoDBContainer = new GenericContainer<>(
        DockerImageName.parse("mongo:latest"))
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "password")
        .withExposedPorts(27017)
        .withCopyFileToContainer(
            forClasspathResource("./init.js"),
            "/docker-entrypoint-initdb.d/init-script.js"
        );

    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongo.baseUrl", "localhost");
        System.setProperty("mongo.port", mappedPort.toString());
        System.setProperty("mongo.username", "root");
        System.setProperty("mongo.password", "password");
    }
}
