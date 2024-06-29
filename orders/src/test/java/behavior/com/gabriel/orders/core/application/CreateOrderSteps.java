package behavior.com.gabriel.orders.core.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.specs.orders.models.OrderRequest;
import io.cloudevents.CloudEvent;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import utils.com.gabriel.orders.core.application.CreateOrderCommandMock;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.SpringContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CucumberContextConfiguration
@SpringBootTest
public class CreateOrderSteps extends SpringContextConfiguration {

    private String authToken;
    private CreateOrderCommand command;
    private ProductID validProductID;
    private IngredientID validIngredientID;
    private OrderRequest validOrderRequest;
    private Order order;
    private Exception exception;

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Before
    public void setup() {
        authToken = null;
        validProductID = new ProductID();
        validIngredientID = new IngredientID();
        menuRepository.addProduct(ProductMock.validProduct(validProductID));
        menuRepository.addExtra(ExtraMock.validExtra(validIngredientID));

        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("orders"));
    }

    @After
    public void collect() {
        if (consumer != null) {
            consumer.close();
        }
    }


    @Given("a logged in customer user")
    public void aLoggedInCustomerUser() {
        authToken = "TOKENWITHGROUPORDERSUSER";
    }

    @When("create a new order")
    public void createANewOrder() {
        Response response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body()
            .when()
            .get("/orders");
    }

    @Given("a create order command with invalid product id")
    public void anCreateOrderCommandWithInvalidProductId() {
        command = CreateOrderCommandMock.validCommand(new ProductID(), validIngredientID);
    }

    @Given("a create order command with invalid extra id")
    public void anCreateOrderCommandWithInvalidExtraId() {
        command = CreateOrderCommandMock.validCommand(validProductID, new IngredientID());
    }

    @Then("the order should be saved in the database")
    public void theOrderShouldBeSavedInTheRepository() {
        assertNotNull(order);
        Order savedOrder = orderRepository.getByTicket(order.getTicketId());
        assertEquals(order.getTicketId(), savedOrder.getTicketId());
    }

    @Then("an order created event should be published")
    public void anOrderCreatedEventShouldBePublished() throws JsonProcessingException {
        ConsumerRecord<String, CloudEvent> record = KafkaTestUtils.getSingleRecord(consumer, "orders");

        assertThat(record).isNotNull();
        CloudEvent receivedEvent = record.value();
        assertThat(receivedEvent).isNotNull();

        byte[] data = Objects.requireNonNull(receivedEvent.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        Order receivedOrder = objectMapper.readValue(json, Order.class);
        assertEquals(receivedOrder.getOrderId().getId(), order.getOrderId().getId());
    }

    @Then("an exception should be thrown with message {string}")
    public void anExceptionShouldBeThrownWithMessageAndCode(String message) {
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }
}
