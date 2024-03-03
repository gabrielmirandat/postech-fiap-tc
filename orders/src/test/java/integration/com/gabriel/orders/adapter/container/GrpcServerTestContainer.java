package integration.com.gabriel.orders.adapter.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

@Testcontainers
public class GrpcServerTestContainer {

    private static final String BASE_ABSOLUTE_PATH = System.getenv("POSTECH_BASE_URL");
    private static final String PROTO_RELATIVE_PATH = "/core/src/main/java/com/gabriel/specs/menu";
    private static final String STUB_RELATIVE_PATH = "/orders/src/test/java/integration/com/gabriel/orders/adapter/container/stubs";
    private static final String GRIPMOCK_IMAGE = "tkpd/gripmock:latest"; // gabrielmirandat/gripmock:latest
    private static final GenericContainer GRPC_CONTAINER;


    static {
        GRPC_CONTAINER = new GenericContainer<>(GRIPMOCK_IMAGE)
            .withNetworkAliases("gripmock")
            .withExposedPorts(4770, 4771)
            .waitingFor(
                new LogMessageWaitStrategy()
                    .withRegEx(".*Serving gRPC on tcp://:4770.*")
                    .withTimes(1)
                    .withStartupTimeout(Duration.ofSeconds(30))
            )
            .withCopyFileToContainer(
                MountableFile.forHostPath(BASE_ABSOLUTE_PATH + PROTO_RELATIVE_PATH), "/proto")
            .withCopyFileToContainer(
                MountableFile.forHostPath(BASE_ABSOLUTE_PATH + STUB_RELATIVE_PATH), "/stub")
            .withCommand("--stub=/stub /proto/menu-api.proto");

        GRPC_CONTAINER.start();

        // Ensure the container is stopped when the JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(GRPC_CONTAINER::stop));
    }

    @DynamicPropertySource
    public static void grpcProperties(DynamicPropertyRegistry registry) {
        registry.add("grpc.menu.server.host", GRPC_CONTAINER::getHost);
        registry.add("grpc.menu.server.port", () -> GRPC_CONTAINER.getMappedPort(4770));
    }
}
