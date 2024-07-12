package behavior.com.gabriel.orders.core.application;

import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.specs.orders.models.OrderResponse;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrieveOrderSteps extends SpringStepsContext {

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;
    }

    @When("retrieving an existing order")
    public void retrieveAnExistingOrder() {
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        Response actualResponse = given()
            .auth().oauth2((String) stateManager.get("AUTH_TOKEN"))
            .when()
            .get("/orders/" + actualTicketId)
            .then()
            .statusCode(200)
            .extract()
            .response();

        stateManager.set("RESPONSE", actualResponse);

        Order actualOrder = OrderMapper.toOrder(actualResponse.as(OrderResponse.class));
        stateManager.set("GENERATED_ORDER", actualOrder);
    }

    @When("retrieving an order with non-existing ticket ID")
    public void retrieveAnOrderWithNonExistingTicketID() {
        String nonExistentTicketId = "12345678";
        Response actualResponse = given()
            .auth().oauth2((String) stateManager.get("AUTH_TOKEN"))
            .when()
            .get("/orders/" + nonExistentTicketId)
            .then()
            .statusCode(404)
            .extract()
            .response();

        stateManager.set("RESPONSE", actualResponse);
    }

    @Then("the order should be retrieved from the database")
    public void theOrderShouldBeRetrievedFromTheDatabase() {
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");
        Order actualOrder = (Order) stateManager.get("GENERATED_ORDER");
        assertNotNull(actualOrder);
        assertEquals(actualTicketId, actualOrder.getTicketId());
    }
}
