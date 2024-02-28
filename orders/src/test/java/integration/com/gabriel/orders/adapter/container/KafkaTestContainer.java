package integration.com.gabriel.orders.adapter.container;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class KafkaTestContainer {

    // Declare the KafkaContainer but do not start it immediately
    private static final KafkaContainer KAFKA_CONTAINER =
        new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.2"));

    @BeforeAll
    public static void startContainer() {
        // Start the container before any tests run in the class
        KAFKA_CONTAINER.start();
    }

    @AfterAll
    public static void stopContainer() {
        // Stop the container after all tests in the class have run
        KAFKA_CONTAINER.stop();
    }

    @DynamicPropertySource
    public static void kafkaProperties(DynamicPropertyRegistry registry) {
        // Dynamic properties to configure Kafka connection for Spring Boot tests
        registry.add("kafka.domain.topic", () -> "orders");
        registry.add("kafka.group.id", () -> "orders-group-id");
        registry.add("kafka.server.url", KAFKA_CONTAINER::getBootstrapServers);
    }
}
