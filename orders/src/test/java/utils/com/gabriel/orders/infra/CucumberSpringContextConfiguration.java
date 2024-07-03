package utils.com.gabriel.orders.infra;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = TestSpringContextConfiguration.class)
public class CucumberSpringContextConfiguration {
}
