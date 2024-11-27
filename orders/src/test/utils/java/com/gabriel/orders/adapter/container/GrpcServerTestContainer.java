package com.gabriel.orders.adapter.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

@Testcontainers
public class GrpcServerTestContainer {

    private static final String GRIPMOCK_IMAGE = "tkpd/gripmock:latest";
    private static final String PROTO_RESOURCE_PATH = "specs/menu-api.proto";
    private static final String STUB_RESOURCE_PATH = "stubs/menu-stub.json";
    private static final GenericContainer<?> GRPC_CONTAINER;

    static {
        GRPC_CONTAINER = new GenericContainer<>(GRIPMOCK_IMAGE)
            .withNetworkAliases("gripmock")
            .withExposedPorts(4770, 4771)
            .waitingFor(
                new LogMessageWaitStrategy()
                    .withRegEx(".*Serving gRPC on tcp://:4770.*")
                    .withTimes(1)
                    .withStartupTimeout(Duration.ofSeconds(30))
            );

        try {
            // Copy the proto file from resources
            GRPC_CONTAINER.withCopyFileToContainer(
                MountableFile.forClasspathResource(PROTO_RESOURCE_PATH),
                "/proto/menu-api.proto"
            );

            // Copy the stub file from resources
            GRPC_CONTAINER.withCopyFileToContainer(
                MountableFile.forClasspathResource(STUB_RESOURCE_PATH),
                "/stub/menu-stub.json"
            );

            GRPC_CONTAINER.withCommand("--stub=/stub /proto/menu-api.proto");
            GRPC_CONTAINER.start();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize GripMock container: " + e.getMessage(), e);
        }

        // Add shutdown hook to stop the container
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (GRPC_CONTAINER.isRunning()) {
                GRPC_CONTAINER.stop();
            }
        }));
    }

    @DynamicPropertySource
    public static void grpcProperties(DynamicPropertyRegistry registry) {
        registry.add("grpc.menu.server.host", GRPC_CONTAINER::getHost);
        registry.add("grpc.menu.server.port", () -> GRPC_CONTAINER.getMappedPort(4770));
    }
}
