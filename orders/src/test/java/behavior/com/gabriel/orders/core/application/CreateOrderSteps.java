package behavior.com.gabriel.orders.core.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Order;
import io.cloudevents.CloudEvent;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.OasConverter;
import utils.com.gabriel.orders.infra.SpringContextConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateOrderSteps extends SpringContextConfiguration {

    private String authToken;
    private ProductID validProductID;
    private IngredientID validIngredientID;
    private String validOrderRequest;
    private String invalidProductOrderRequest;
    private String invalidExtraOrderRequest;
    private String generatedTicketId;
    private Order generatedOrder;
    private Response response;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;

        authToken = null;
        validProductID = new ProductID("11111111-PRDC-1111-11-11");
        validIngredientID = new IngredientID("11111111-INGR-1111-11-11");
        validOrderRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml", "paths:/orders:post:requestBody:content:application/json:examples:CREATE_ORDER_SUCCESS:value");
        invalidProductOrderRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml", "paths:/orders:post:requestBody:content:application/json:examples:CREATE_ORDER_INVALID_PRODUCT:value");
        invalidExtraOrderRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml", "paths:/orders:post:requestBody:content:application/json:examples:CREATE_ORDER_INVALID_EXTRA:value");

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
        response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(validOrderRequest)
            .when()
            .post("/orders")
            .then()
            .statusCode(201)
            .extract()
            .response();

        generatedTicketId = response.path("ticketId");
    }

    @Then("the order should be saved in the database")
    public void theOrderShouldBeSavedInTheRepository() {
        assertNotNull(generatedTicketId);
        generatedOrder = orderRepository.getByTicket(generatedTicketId);
        assertEquals(generatedTicketId, generatedOrder.getTicketId());
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
        assertEquals(receivedOrder.getOrderId().getId(), generatedOrder.getOrderId().getId());
    }

    @When("create a new order with invalid product id")
    public void createANewOrderWithInvalidProductId() {
        response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(invalidProductOrderRequest)
            .when()
            .post("/orders")
            .then()
            .statusCode(400)
            .extract()
            .response();
    }

    @When("create a new order with invalid extra id")
    public void createANewOrderWithInvalidExtraId() {
        response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(invalidExtraOrderRequest)
            .when()
            .post("/orders")
            .then()
            .statusCode(400)
            .extract()
            .response();
    }

    @Then("an error response with message {string} should be returned")
    public void anErrorResponseWithMessageShouldBeReturned(String message) {
        String errorMessage = response.path("message");
        assertEquals(message, errorMessage);
    }
}
