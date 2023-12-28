package adapters;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Configuration
@EnableMongoRepositories
public class MongoDBTestContainerConfig {
    @Container
    public static final GenericContainer<?> mongoDBContainer = new GenericContainer<>(
        DockerImageName.parse("mongo:latest"))
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "password")
        .withExposedPorts(27017)
        .withCopyFileToContainer(MountableFile.forClasspathResource("./init.js"), "/docker-entrypoint-initdb.d/init-script.js");

    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongo.url", "mongodb://root:password@localhost:" + mappedPort + "/?authSource=admin");
    }
}
