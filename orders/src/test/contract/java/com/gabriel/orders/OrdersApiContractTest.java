package com.gabriel.orders;

import com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import com.gabriel.orders.adapter.container.KafkaTestContainer;
import com.gabriel.orders.adapter.container.MongoDBTestContainer;
import com.gabriel.orders.adapter.container.RedisTestContainer;
import com.gabriel.orders.infra.grpc.GrpcClientConfig;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.mongodb.MongoDbConfig;
import com.gabriel.orders.infra.redis.RedisConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import in.specmatic.test.SpecmaticJUnitSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // This will start the server on a random port
@Import({MongoDbConfig.class, GrpcClientConfig.class, RedisConfig.class, KafkaConfig.class, SerializerConfig.class})
@ContextConfiguration(classes =
    {OrdersApplication.class, MongoDBTestContainer.class, GrpcServerTestContainer.class, RedisTestContainer.class, KafkaTestContainer.class}
)
public class OrdersApiContractTest extends SpecmaticJUnitSupport {

    @LocalServerPort
    private int port; // Injects the port the server is running on

    @BeforeAll
    public static void setup() {
        KafkaTestContainer.startContainer();
        File apiContract = new File("src/main/resources/oas/orders-api.yaml");
        System.setProperty("contractPaths", apiContract.getAbsolutePath());
    }

    @AfterAll
    public static void stop() {
        KafkaTestContainer.stopContainer();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        MongoDBTestContainer.mongoProperties(registry);
        GrpcServerTestContainer.grpcProperties(registry);
        RedisTestContainer.redisProperties(registry);
        KafkaTestContainer.kafkaProperties(registry);
    }

    @BeforeEach
    public void initialize() {
        // Now using the dynamically assigned port
        System.setProperty("host", "localhost");
        System.setProperty("port", String.valueOf(port));
    }
}
