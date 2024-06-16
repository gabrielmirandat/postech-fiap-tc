package behavior.com.gabriel.orders.core.application.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CreateOrderSteps {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private OrderPublisher orderPublisher;

    private CreateOrderCommand command;
    private Order order;
    private Exception exception;

    @Given("a valid create order command")
    public void aValidCreateOrderCommand() {
        command = new CreateOrderCommand(
            null, null, null, null
        );
        // Populate the command with valid data
    }

    @Given("an invalid create order command")
    public void anInvalidCreateOrderCommand() {
        command = new CreateOrderCommand(
            null, null, null, null
        );
        // Populate the command with invalid data
    }

    @When("I create a new order")
    public void iCreateANewOrder() {
        try {
            order = createOrderUseCase.createOrder(command);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the order should be saved in the repository")
    public void theOrderShouldBeSavedInTheRepository() throws JsonProcessingException {
        assertNotNull(order);
        verify(createOrderUseCase, times(1)).createOrder(command);
    }

    @Then("an order created event should be published")
    public void anOrderCreatedEventShouldBePublished() throws JsonProcessingException {
        // Verifique que o evento OrderCreatedEvent foi publicado
        verify(orderPublisher, times(1)).orderCreated(ArgumentMatchers.any(OrderCreatedEvent.class));
    }

    @Then("an exception should be thrown")
    public void anExceptionShouldBeThrown() {
        assertNotNull(exception);
    }
}
