package behavior.com.gabriel.orders.core.application.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.OrdersApplication;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.infra.grpc.MenuGrpcClientConfig;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.mongodb.MongoDbConfig;
import com.gabriel.orders.infra.redis.RedisConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import utils.com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import utils.com.gabriel.orders.adapter.container.KafkaTestContainer;
import utils.com.gabriel.orders.adapter.container.MongoDBTestContainer;
import utils.com.gabriel.orders.adapter.container.RedisTestContainer;
import utils.com.gabriel.orders.core.application.CreateOrderCommandMock;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.SecurityConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import({MongoDbConfig.class, MenuGrpcClientConfig.class, RedisConfig.class,
    KafkaConfig.class, SerializerConfig.class, SecurityConfig.class})
@ContextConfiguration(classes = {OrdersApplication.class, MongoDBTestContainer.class, GrpcServerTestContainer.class,
    RedisTestContainer.class, KafkaTestContainer.class})
@ActiveProfiles("test")
public class CreateOrderSteps {

    private CreateOrderCommand command;
    private ProductID validProductID;
    private IngredientID validIngredientID;
    private Order order;
    private Exception exception;

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private OrderPublisher orderPublisher;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    public void setUp() {
        validProductID = new ProductID();
        validIngredientID = new IngredientID();
        menuRepository.addProduct(ProductMock.validProduct(validProductID));
        menuRepository.addExtra(ExtraMock.validExtra(validIngredientID));
    }

    @Given("a valid create order command")
    public void aValidCreateOrderCommand() {
        command = CreateOrderCommandMock.validCommand(validProductID, validIngredientID);
    }

    @Given("an invalid create order command")
    public void anInvalidCreateOrderCommand() {
        command = new CreateOrderCommand(null, null, null, null);
    }

    @When("I create a new order")
    public void iCreateANewOrder() {
        try {
            order = createOrderUseCase.createOrder(command);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the order should be saved in the database")
    public void theOrderShouldBeSavedInTheRepository() {
        assertNotNull(order);
        Order savedOrder = orderRepository.getByTicket(order.getTicketId());
        assertEquals(order, savedOrder);
    }

    @Then("an order created event should be published")
    public void anOrderCreatedEventShouldBePublished() throws JsonProcessingException {
        verify(orderPublisher).orderCreated(any(OrderCreatedEvent.class));
    }

    @Then("an exception should be thrown")
    public void anExceptionShouldBeThrown() {
        assertNotNull(exception);
    }
}
