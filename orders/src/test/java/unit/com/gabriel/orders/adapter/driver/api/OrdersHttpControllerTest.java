package unit.com.gabriel.orders.adapter.driver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.adapter.driver.api.OrdersHttpController;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.application.usecase.ProcessOrderUseCase;
import com.gabriel.orders.core.application.usecase.RetrieveOrderUseCase;
import com.gabriel.orders.core.application.usecase.SearchOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.specs.orders.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import unit.com.gabriel.orders.core.OrderMock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class OrdersHttpControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private RetrieveOrderUseCase retrieveOrderUseCase;

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @Mock
    private SearchOrderUseCase searchOrderUseCase;

    @InjectMocks
    private OrdersHttpController controller;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = OrderMock.generateBasic();
        openMocks(this);

        // Set up MockHttpServletRequest
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);

        // Set the request attributes to the RequestContextHolder
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void addOrder_Success() throws JsonProcessingException {
        OrderRequest orderRequest = new OrderRequest(); // Setup your OrderRequest

        when(createOrderUseCase.createOrder(any())).thenReturn(order);

        ResponseEntity<OrderCreated> response = controller.addOrder(orderRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(order.getTicketId(), response.getBody().getTicketId());
    }

    @Test
    void getOrderById_Success() {
        when(retrieveOrderUseCase.getByTicketId(any())).thenReturn(order);

        ResponseEntity<OrderResponse> response = controller.getOrderById("12345678");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        // Additional assertions based on OrderResponse structure
    }

    @Test
    void changeOrderStatus_Success() throws Exception {
        ResponseEntity<OrderUpdated> response = controller.changeOrderStatus("12345678", OrderStatusDTO.COMPLETED);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void findOrdersByQuery_Success() throws Exception {
        List<Order> orders = Arrays.asList(order, order); // Setup your Order list

        when(searchOrderUseCase.searchBy(any())).thenReturn(orders);

        ResponseEntity<List<OrderResponse>> response = controller.findOrdersByQuery(Optional.of(OrderStatusDTO.CREATED));

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
