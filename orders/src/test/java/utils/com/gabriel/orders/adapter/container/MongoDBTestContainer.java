package utils.com.gabriel.orders.adapter.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MongoDBTestContainer {

    private static final MongoDBContainer MONGO_DB_CONTAINER;

    static {
        MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:4.4.3"))
            .withEnv("MONGO_INITDB_DATABASE", "postech_db");
        MONGO_DB_CONTAINER.start();

        // Ensure the container is stopped when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(MONGO_DB_CONTAINER::stop));
    }

    @DynamicPropertySource
    public static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("mongodb.conn.string", () ->
            MONGO_DB_CONTAINER.getReplicaSetUrl("postech_db"));
        registry.add("mongodb.conn.db", () -> "postech_db");
    }
}
