package behavior.com.gabriel.orders.core.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Order;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;
import utils.com.gabriel.orders.infra.OasConverter;
import utils.com.gabriel.orders.infra.TestSpringContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetrieveOrderSteps extends TestSpringContextConfiguration {

    private final ProductID existingProductID = new ProductID("11111111-PRDC-1111-11-11");
    private final IngredientID existingIngredientID = new IngredientID("11111111-INGR-1111-11-11");
    private String authToken = null;
    private String validOrderRequest;
    private String generatedTicketId;
    private Order generatedOrder;
    private Response response;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        RestAssured.port = port;

        menuRepository.addProduct(ProductMock.validProduct(existingProductID));
        menuRepository.addExtra(ExtraMock.validExtra(existingIngredientID));

        createValidOrder();
    }

    @Given("a logged in customer user")
    public void aLoggedInCustomerUser() {
        authToken = "TOKENWITHGROUPORDERSUSER";
    }

    private void createValidOrder() throws Exception {
        validOrderRequest = OasConverter.convertSpecToJson("/oas/orders-api.yaml",
            "paths:/orders:post:requestBody:content:application/json:examples:CREATE_ORDER_SUCCESS:value");

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

    @When("retrieve an existing order")
    public void retrieveAnExistingOrder() {
        response = given()
            .auth().oauth2(authToken)
            .when()
            .get("/orders/" + generatedTicketId)
            .then()
            .statusCode(200)
            .extract()
            .response();

        generatedOrder = response.as(Order.class);
    }

    @When("retrieve an order with non-existing ticket ID")
    public void retrieveAnOrderWithNonExistingTicketID() {
        String nonExistentTicketId = "12345678";
        response = given()
            .auth().oauth2(authToken)
            .when()
            .get("/orders/" + nonExistentTicketId)
            .then()
            .statusCode(404)
            .extract()
            .response();
    }

    @Then("the order should be retrieved from the database")
    public void theOrderShouldBeRetrievedFromTheDatabase() {
        assertNotNull(generatedOrder);
        assertEquals(generatedTicketId, generatedOrder.getTicketId());
    }

    @Then("an error {string} - {string} should be returned")
    public void anErrorResponseWithCodeAndMessageShouldBeReturned(String code, String message) {
        assertEquals(code, response.path("code"));
        assertEquals(message, response.path("message"));
    }
}
