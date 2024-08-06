package behavior.com.gabriel.orders.core.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CancelOrderSteps extends SpringStepsContext {

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;
    }

    @When("canceling the order")
    public void cancelingOrder() {
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        String authToken = (String) stateManager.get("AUTH_TOKEN");

        Response actualResponse = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/orders/" + actualTicketId)
            .then()
            .extract()
            .response();

        stateManager.set("RESPONSE", actualResponse);
        stateManager.set("RECORDED_EVENT", KafkaTestUtils.getSingleRecord(consumer, "orders"));
    }

    @Then("an order canceled event should be published")
    public void anOrderCreatedEventShouldBePublished() throws JsonProcessingException {
        ConsumerRecord<String, CloudEvent> actualEvent =
            (ConsumerRecord<String, CloudEvent>) stateManager.get("RECORDED_EVENT");

        assertThat(actualEvent).isNotNull();
        CloudEvent receivedEvent = actualEvent.value();
        assertThat(receivedEvent).isNotNull();

        byte[] data = Objects.requireNonNull(receivedEvent.getData()).toBytes();
        String json = new String(data, StandardCharsets.UTF_8);
        String receivedTickedId = objectMapper.readValue(json, String.class);
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        assertEquals(receivedTickedId, actualTicketId);
    }
}
