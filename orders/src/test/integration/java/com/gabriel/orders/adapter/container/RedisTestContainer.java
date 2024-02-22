package com.gabriel.orders.adapter.container;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisTestContainer {

    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        // Specify the Redis image to use
        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379); // Redis default port
        REDIS_CONTAINER.start();

        // Ensure the container is stopped when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(REDIS_CONTAINER::stop));
    }

    @DynamicPropertySource
    public static void redisProperties(DynamicPropertyRegistry registry) {
        String redisUrl = String.format("redis://%s:%d",
            REDIS_CONTAINER.getHost(), REDIS_CONTAINER.getFirstMappedPort());

        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.data.redis.url", () -> redisUrl);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(REDIS_CONTAINER.getHost(),
            REDIS_CONTAINER.getFirstMappedPort());
    }
}
