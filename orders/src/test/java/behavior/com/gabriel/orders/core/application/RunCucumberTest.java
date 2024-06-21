package behavior.com.gabriel.orders.core.application;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/behavior/com/gabriel/orders/core/application/features",
    glue = "behavior.com.gabriel.orders.core.application.steps",
    plugin = {"pretty", "html:target/cucumber"}
)
public class RunCucumberTest {
}
