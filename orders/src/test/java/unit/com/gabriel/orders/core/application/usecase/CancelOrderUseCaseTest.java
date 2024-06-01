package unit.com.gabriel.orders.core.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.core.application.command.CancelOrderCommand;
import com.gabriel.orders.core.application.usecase.CancelOrderUseCase;
import com.gabriel.orders.core.domain.event.OrderDeletedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.TicketId;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import utils.com.gabriel.orders.core.OrderMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CancelOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderPublisher orderPublisher;

    @Captor
    private ArgumentCaptor<Order> canceledOrderCaptor;

    @Captor
    private ArgumentCaptor<OrderDeletedEvent> orderDeletedEventCaptor;

    @InjectMocks
    private CancelOrderUseCase cancelOrderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCancelOrder() throws JsonProcessingException {
        // Given
        Order mockOrder = OrderMock.generateBasic();

        // When
        when(orderRepository.getByTicket(any())).thenReturn(mockOrder);
        when(orderRepository.updateOrder(any())).thenReturn(mockOrder);
        doNothing().when(orderPublisher).orderCanceled(any());

        cancelOrderUseCase.cancelOrder(new CancelOrderCommand(new TicketId(mockOrder.getTicketId())));

        // Then
        verify(orderRepository).getByTicket(eq(mockOrder.getTicketId()));
        verify(orderRepository).updateOrder(canceledOrderCaptor.capture());
        verify(orderPublisher).orderCanceled(orderDeletedEventCaptor.capture());
        assertThat(canceledOrderCaptor.getValue().getTicketId()).isEqualTo(mockOrder.getTicketId());
        assertThat(canceledOrderCaptor.getValue().getStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThat(orderDeletedEventCaptor.getValue().getTicketId()).isEqualTo(mockOrder.getTicketId());
    }
}
