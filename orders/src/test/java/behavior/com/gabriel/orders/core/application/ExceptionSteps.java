package behavior.com.gabriel.orders.core.application;

import io.cucumber.java.en.Then;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionSteps extends CucumberSpringContextConfiguration {

    @Then("an error {string} - {string} should be returned")
    public void anErrorResponseWithCodeAndMessageShouldBeReturned(String code, String message) {
        assertEquals(code, response.path("code"));
        assertEquals(message, response.path("message"));
    }
}
