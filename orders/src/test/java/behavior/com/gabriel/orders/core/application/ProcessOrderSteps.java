package behavior.com.gabriel.orders.core.application;

import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import utils.com.gabriel.orders.infra.OasConverter;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProcessOrderSteps extends SpringStepsContext {

    private String changeOrderStatusRequest;

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;

        changeOrderStatusRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml",
            "paths:/orders/{id}/status/{status}:post:requestBody:content:application/json:examples:CHANGE_ORDER_STATUS_SUCCESS:value");
    }

    @When("process the order through statuses")
    public void processOrderThroughStatuses(List<String> statuses) {
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        String authToken = (String) stateManager.get("AUTH_TOKEN");

        for (String status : statuses) {
            String requestBody = changeOrderStatusRequest.replace("{id}", actualTicketId)
                .replace("{status}", status.toUpperCase());

            Response actualResponse = given()
                .auth().oauth2(authToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post("/orders/" + actualTicketId + "/status/" + status)
                .then()
                .extract()
                .response();

            stateManager.set("RESPONSE", actualResponse);
        }
    }

    @When("finishing the order")
    public void finishingOrder() {
        processOrderThroughStatuses(List.of("COMPLETED"));
    }

    @Then("the order should be updated to status {string}")
    public void theOrderShouldBeUpdatedToStatus(String status) {
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        Order actualOrder = orderRepository.getByTicket(actualTicketId);
        assertNotNull(actualOrder);
        assertEquals(OrderStatus.valueOf(status.toUpperCase()), actualOrder.getStatus());
    }
}
