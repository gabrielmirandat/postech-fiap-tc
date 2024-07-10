package behavior.com.gabriel.orders.core.application;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionSteps extends SpringStepsContext {

    @Then("an error {string} - {string} should be returned")
    public void anErrorResponseWithCodeAndMessageShouldBeReturned(String code, String message) {
        Response actualResponse = (Response) stateManager.get("RESPONSE");
        assertEquals(code, actualResponse.path("code"));
        assertEquals(message, actualResponse.path("message"));
    }

    @Then("an error message {string} should be returned")
    public void anErrorResponseWithMessageShouldBeReturned(String message) {
        Response actualResponse = (Response) stateManager.get("RESPONSE");
        assertEquals(message, actualResponse.path("message"));
    }
}
