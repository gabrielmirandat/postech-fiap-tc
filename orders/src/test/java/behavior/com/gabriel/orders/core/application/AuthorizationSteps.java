package behavior.com.gabriel.orders.core.application;

import io.cucumber.java.en.Given;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationSteps extends SpringStepsContext {

    @Given("a logged in customer user")
    public void aLoggedInCustomerUser() {
        stateManager.set("AUTH_TOKEN", "TOKENWITHGROUPORDERSUSER");
    }

    @Given("a logged in squad orders user")
    public void aLoggedInSquadOrdersUser() {
        stateManager.set("AUTH_TOKEN", "TOKENWITHGROUPORDERSSQUAD");
    }

    @Given("a logged in admin orders user")
    public void aLoggedInAdminOrdersUser() {
        stateManager.set("AUTH_TOKEN", "TOKENWITHGROUPORDERSADMIN");
    }
}
