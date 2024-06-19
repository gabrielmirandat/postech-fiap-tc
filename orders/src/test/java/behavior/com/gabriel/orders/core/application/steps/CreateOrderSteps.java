package behavior.com.gabriel.orders.core.application.steps;

import behavior.com.gabriel.orders.core.application.CucumberSpringConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.Order;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import utils.com.gabriel.orders.core.application.CreateOrderCommandMock;
import utils.com.gabriel.orders.core.domain.ExtraMock;
import utils.com.gabriel.orders.core.domain.ProductMock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CucumberContextConfiguration
public class CreateOrderSteps extends CucumberSpringConfiguration {

    private CreateOrderCommand command;
    private ProductID validProductID;
    private IngredientID validIngredientID;
    private Order order;
    private Exception exception;


    @Before
    public void setup() {
        validProductID = new ProductID();
        validIngredientID = new IngredientID();
        menuRepository.addProduct(ProductMock.validProduct(validProductID));
        menuRepository.addExtra(ExtraMock.validExtra(validIngredientID));
    }

    @Given("a valid create order command")
    public void aValidCreateOrderCommand() {
        command = CreateOrderCommandMock.validCommand(validProductID, validIngredientID);
    }

    @Given("an invalid create order command")
    public void anInvalidCreateOrderCommand() {
        command = new CreateOrderCommand(null, null, null, null);
    }

    @When("I create a new order")
    public void iCreateANewOrder() {
        try {
            order = createOrderUseCase.createOrder(command);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the order should be saved in the database")
    public void theOrderShouldBeSavedInTheRepository() {
        assertNotNull(order);
        Order savedOrder = orderRepository.getByTicket(order.getTicketId());
        assertEquals(order.getTicketId(), savedOrder.getTicketId());
    }

    @Then("an order created event should be published")
    public void anOrderCreatedEventShouldBePublished() throws JsonProcessingException {
        //verify(orderPublisher).orderCreated(any(OrderCreatedEvent.class));
    }

    @Then("an exception should be thrown")
    public void anExceptionShouldBeThrown() {
        assertNotNull(exception);
    }
}
