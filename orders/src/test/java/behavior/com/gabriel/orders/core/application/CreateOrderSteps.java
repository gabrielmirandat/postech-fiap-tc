package behavior.com.gabriel.orders.core.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Order;
import io.cloudevents.CloudEvent;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.OasConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateOrderSteps extends CucumberSpringContextConfiguration {

    private final ProductID existingProductID = new ProductID("11111111-PRDC-1111-11-11");
    private final IngredientID existingIngredientID = new IngredientID("11111111-INGR-1111-11-11");
    private final ProductID nonExistentProductID = new ProductID("11111111-PRDC-1111-11-12");
    private final IngredientID nonExistentIngredientID = new IngredientID("11111111-INGR-1111-11-12");
    private String validOrderRequest;
    private String invalidProductOrderRequest;
    private String invalidExtraOrderRequest;
    private String generatedTicketId;
    private Order generatedOrder;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;

        validOrderRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml",
            "paths:/orders:post:requestBody:content:application/json:examples:CREATE_ORDER_SUCCESS:value");

        invalidProductOrderRequest = validOrderRequest.replaceAll(existingProductID.getId(), nonExistentProductID.getId());

        invalidExtraOrderRequest = validOrderRequest.replaceAll(existingIngredientID.getId(), nonExistentIngredientID.getId());

        menuRepository.addProduct(ProductMock.validProduct(existingProductID));
        menuRepository.addExtra(ExtraMock.validExtra(existingIngredientID));

        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("orders"));
    }

    @After
    public void collect() {
        if (consumer != null) {
            consumer.close();
        }
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

    @When("create a new order with non-existing product")
    public void createANewOrderWithNonExistingProductId() {
        response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(invalidProductOrderRequest)
            .when()
            .post("/orders")
            .then()
            .statusCode(422)
            .extract()
            .response();
    }

    @When("create a new order with non-existing extra")
    public void createANewOrderWithNonExistingExtraId() {
        response = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(invalidExtraOrderRequest)
            .when()
            .post("/orders")
            .then()
            .statusCode(422)
            .extract()
            .response();
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
}
