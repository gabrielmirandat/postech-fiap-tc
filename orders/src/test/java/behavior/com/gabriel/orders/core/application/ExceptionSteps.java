package behavior.com.gabriel.orders.core.application;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionSteps extends SpringStepsContext {

    @Then("an error {string} - {string} - {string} should be returned")
    public void anErrorResponseWithStatusCodeAndMessageShouldBeReturned(String status, String code,
                                                                        String message) {
        Response actualResponse = (Response) stateManager.get("RESPONSE");
        assertEquals(status, actualResponse.path("status").toString());
        assertEquals(code, actualResponse.path("code"));
        assertEquals(message, actualResponse.path("message"));
    }

    @Then("an error {string} - {string} should be returned")
    public void anErrorResponseWithStatusAndMessageShouldBeReturned(String status, String message) {
        Response actualResponse = (Response) stateManager.get("RESPONSE");
        assertEquals(status, actualResponse.path("status").toString());
        assertEquals(message, actualResponse.path("message"));
    }
}
