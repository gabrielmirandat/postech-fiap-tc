package utils.com.gabriel.orders.infra;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/behavior/com/gabriel/orders/core/application/usecase",
    plugin = {"pretty", "html:target/cucumber-reports"},
    glue = "behavior.com.gabriel.orders.core.application.steps")
public class CucumberTest {
}
