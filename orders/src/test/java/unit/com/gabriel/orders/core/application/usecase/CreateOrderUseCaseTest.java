package unit.com.gabriel.orders.core.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.application.usecase.VerifyMenuUseCase;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.com.gabriel.orders.core.application.CreateOrderCommandMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderPublisher orderPublisher;

    @Mock
    private VerifyMenuUseCase verifyMenuUseCase;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() throws JsonProcessingException {
        // Given
        ProductID productId = new ProductID();
        IngredientID ingredientId = new IngredientID();
        CreateOrderCommand command = CreateOrderCommandMock.validCommand(productId, ingredientId);

        when(menuRepository.getProduct(any())).thenReturn(new Product(productId, "product", 10.0)); // Customize this as necessary
        when(menuRepository.getExtra(any())).thenReturn(new Extra(ingredientId, "extra", 2.0)); // Customize this as necessary

        // When
        Order createdOrder = createOrderUseCase.createOrder(command);

        // Then
        verify(verifyMenuUseCase).validate(command);
        verify(menuRepository).getProduct(productId);
        verify(menuRepository).getExtra(ingredientId);
        verify(orderRepository).saveOrder(createdOrder);
        verify(orderPublisher).orderCreated(argThat(event -> event.getOrderCreated().equals(createdOrder)));

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getCustomer()).isEqualTo(command.customer());
        assertThat(createdOrder.getShippingAddress()).isEqualTo(command.shippingAddress());
        assertThat(createdOrder.getNotification()).isEqualTo(command.notification());
        assertThat(createdOrder.getItems()).hasSize(1);
        assertThat(createdOrder.getItems().get(0).getProduct().getProductID()).isEqualTo(productId);
        assertThat(createdOrder.getItems().get(0).getExtras()).hasSize(1);
        assertThat(createdOrder.getItems().get(0).getExtras().get(0).getIngredientID()).isEqualTo(ingredientId);
    }
}
