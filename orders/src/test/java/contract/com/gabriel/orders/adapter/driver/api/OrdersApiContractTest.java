package contract.com.gabriel.orders.adapter.driver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.OrdersApplication;
import com.gabriel.orders.adapter.driven.api.MenuGrpcClient;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.*;
import com.gabriel.orders.infra.grpc.MenuGrpcClientConfig;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.mongodb.MongoDbConfig;
import com.gabriel.orders.infra.redis.RedisConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import in.specmatic.test.SpecmaticJUnitSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import utils.com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import utils.com.gabriel.orders.adapter.container.KafkaTestContainer;
import utils.com.gabriel.orders.adapter.container.MongoDBTestContainer;
import utils.com.gabriel.orders.adapter.container.RedisTestContainer;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.OrderMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.SecurityConfig;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // This will start the server on a random port
@Import({MongoDbConfig.class, MenuGrpcClientConfig.class, RedisConfig.class,
    KafkaConfig.class, SerializerConfig.class, SecurityConfig.class})
@ContextConfiguration(classes =
    {OrdersApplication.class, MongoDBTestContainer.class, GrpcServerTestContainer.class,
        RedisTestContainer.class, KafkaTestContainer.class}
)
@ActiveProfiles("test")
public class OrdersApiContractTest extends SpecmaticJUnitSupport {

    @MockBean
    private MenuRepository menuRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private MenuGrpcClient menuGrpcClient;
    @MockBean
    private OrderPublisher orderPublisher;
    @MockBean
    private MenuSubscriber menuSubscriber;
    @MockBean
    private JwtDecoder jwtDecoder;

    @LocalServerPort
    private int port;

    private Order order;
    private Order orderFull;
    private Product product;
    private Extra extra;

    @BeforeAll
    public static void setup() {
        File apiContract = new File("src/main/resources/oas/orders-api.yaml");
        System.setProperty("contractPaths", apiContract.getAbsolutePath());
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        MongoDBTestContainer.mongoProperties(registry);
        GrpcServerTestContainer.grpcProperties(registry);
        RedisTestContainer.redisProperties(registry);
        KafkaTestContainer.kafkaProperties(registry);
    }

    @BeforeEach
    public void initialize() throws JsonProcessingException {
        SecurityContextHolder.clearContext();

        orderFull = OrderMock.validFullOrder();
        order = OrderMock.validBasicOrder();
        product = ProductMock.validProduct(new ProductID("11111111-PRDC-1111-11-11"));
        extra = ExtraMock.validExtra(new IngredientID("11111111-INGR-1111-11-11"));

        when(orderRepository.getByTicket(eq("11111111")))
            .thenReturn(orderFull);
        when(orderRepository.getByTicket(eq("11111112")))
            .thenReturn(order);
        when(orderRepository.getByTicket(eq("11111113")))
            .thenThrow(new NotFound("Could not find order with ticketId 11111113"));
        when(orderRepository.getByTicket(eq("11111119")))
            .thenThrow(RuntimeException.class);
        when(orderRepository.searchBy(eq(new OrderSearchParameters(OrderStatus.CREATED))))
            .thenReturn(List.of(orderFull));
        when(menuRepository.getProduct(eq(new ProductID("11111111-PRDC-1111-11-11"))))
            .thenReturn(product);
        when(menuRepository.existsProduct(eq(new ProductID("11111111-PRDC-1111-11-11"))))
            .thenReturn(true);
        when(menuRepository.existsProduct(eq(new ProductID("11111111-PRDC-1111-11-19"))))
            .thenThrow(RuntimeException.class);
        when(menuRepository.getExtra(eq(new IngredientID("11111111-INGR-1111-11-11"))))
            .thenReturn(extra);
        when(menuRepository.existsExtra(eq(new IngredientID("11111111-INGR-1111-11-11"))))
            .thenReturn(true);

        System.setProperty("host", "localhost");
        System.setProperty("port", String.valueOf(port));
    }

    @AfterEach
    public void cleanup() {
        Mockito.reset(); // Correctly clears all mocks
    }
}
