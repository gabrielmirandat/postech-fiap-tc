package unit.com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.command.ProcessOrderCommand;
import com.gabriel.orders.core.application.usecase.ProcessOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.TicketId;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import utils.com.gabriel.orders.core.OrderMock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessOrderUseCaseTest {

    @Captor
    ArgumentCaptor<Order> orderCaptor;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private ProcessOrderUseCase processOrderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessOrder() {
        // Given
        TicketId id = new TicketId("11111111");
        OrderStatus status = OrderStatus.PREPARATION;
        ProcessOrderCommand command = new ProcessOrderCommand(id, status);

        Order mockOrder = OrderMock.generateBasic();
        when(orderRepository.getByTicket(id.getId())).thenReturn(mockOrder);

        // When
        processOrderUseCase.processOrder(command);

        // Then
        verify(orderRepository).getByTicket(id.getId());
        verify(orderRepository).updateOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(status, capturedOrder.getStatus(), "The order status should be updated.");
    }
}
