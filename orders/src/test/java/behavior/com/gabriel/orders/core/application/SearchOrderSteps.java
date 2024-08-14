package behavior.com.gabriel.orders.core.application;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class SearchOrderSteps extends SpringStepsContext {

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;
    }

    @When("search for orders by status {string}")
    public void searchForOrdersByStatus(String status) {
        String authToken = (String) stateManager.get("AUTH_TOKEN");

        Response actualResponse = given()
            .auth().oauth2(authToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/orders?status=" + status)
            .then()
            .extract()
            .response();

        stateManager.set("RESPONSE", actualResponse);
    }

    @Then("the order should be in the search results")
    public void theOrderShouldBeInSearchResults() {
        Response actualResponse = (Response) stateManager.get("RESPONSE");
        String actualTicketId = (String) stateManager.get("GENERATED_TICKET_ID");

        actualResponse.then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", org.hamcrest.Matchers.greaterThan(0))
            .body("[0].status", org.hamcrest.Matchers.equalTo("CREATED"))
            .body("[0].ticketId", org.hamcrest.Matchers.equalTo(actualTicketId));
    }

    @Then("the order should not be in the search results")
    public void theOrderShouldNotBeInSearchResults() {
        Response actualResponse = (Response) stateManager.get("RESPONSE");

        actualResponse.then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", org.hamcrest.Matchers.equalTo(0));
    }
}
