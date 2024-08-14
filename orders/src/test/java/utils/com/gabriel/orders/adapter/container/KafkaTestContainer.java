package utils.com.gabriel.orders.adapter.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class KafkaTestContainer {

    private static final KafkaContainer KAFKA_CONTAINER;

    static {
        KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.2"));
        KAFKA_CONTAINER.start();

        // Ensure the container is stopped when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(KAFKA_CONTAINER::stop));
    }

    @DynamicPropertySource
    public static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.server.url", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("kafka.group.id", () -> "test-group-id-" + System.currentTimeMillis()); // Unique group ID
    }
}
