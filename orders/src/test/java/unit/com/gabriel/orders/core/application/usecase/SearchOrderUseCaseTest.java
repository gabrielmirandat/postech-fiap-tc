package unit.com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.query.SearchByOrderStatusQuery;
import com.gabriel.orders.core.application.usecase.SearchOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.com.gabriel.orders.core.OrderMock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SearchOrderUseCase searchOrderUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchBy() {
        // Given
        OrderStatus status = OrderStatus.CREATED;
        SearchByOrderStatusQuery query = new SearchByOrderStatusQuery(status);
        List<Order> expectedOrders = Arrays.asList(OrderMock.generateBasic(), OrderMock.generateBasic());

        when(orderRepository.searchBy(new OrderSearchParameters(status))).thenReturn(expectedOrders);

        // When
        List<Order> actualOrders = searchOrderUseCase.searchBy(query);

        // Then
        verify(orderRepository).searchBy(new OrderSearchParameters(status));
        assertEquals(expectedOrders, actualOrders, "The list of orders returned should match the expected list.");
    }
}
