package integration.com.gabriel.orders.adapter.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class GrpcServerTestContainer {

    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String PROTO_RELATIVE_PATH = "/../core/src/main/java/com/gabriel/specs/menu";
    private static final String STUB_RELATIVE_PATH = "/src/test/java/integration/com/gabriel/orders/adapter/container/stubs";

    private static final GenericContainer GRPC_CONTAINER;

    static {
        GRPC_CONTAINER = new GenericContainer<>("gabrielmirandat/gripmock:latest")
            .withExposedPorts(4770, 4771) // Default gRPC port for GripMock
            .withCopyFileToContainer(
                MountableFile.forHostPath(PROJECT_ROOT + PROTO_RELATIVE_PATH), "/proto")
            .withCopyFileToContainer(
                MountableFile.forHostPath(PROJECT_ROOT + STUB_RELATIVE_PATH), "/stub")
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
