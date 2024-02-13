package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetrieveOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RetrieveOrderUseCase retrieveOrderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetByTicketId() {
        // Given
        String ticketId = "testTicketId";
        GetByTicketOrderQuery query = new GetByTicketOrderQuery(ticketId);
        Order expectedOrder = OrderMock.generateBasic();
        // If your Order class requires specific setup, do it here
        when(orderRepository.getByTicket(ticketId)).thenReturn(expectedOrder);

        // When
        Order actualOrder = retrieveOrderUseCase.getByTicketId(query);

        // Then
        verify(orderRepository).getByTicket(ticketId);
        assertEquals(expectedOrder, actualOrder, "The retrieved order should match the expected one.");
    }
}
