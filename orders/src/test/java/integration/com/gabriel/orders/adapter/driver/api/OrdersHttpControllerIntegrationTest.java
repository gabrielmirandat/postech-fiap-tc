package integration.com.gabriel.orders.adapter.driver.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.adapter.driver.api.OrdersHttpController;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.usecase.*;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.PermissionRepository;
import com.gabriel.specs.orders.models.OrderItemRequest;
import com.gabriel.specs.orders.models.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import utils.com.gabriel.orders.core.OrderMock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = OrdersHttpController.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class OrdersHttpControllerIntegrationTest {

    private final ObjectMapper objectMapper = objectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private CancelOrderUseCase cancelOrderUseCase;

    @MockBean
    private RetrieveOrderUseCase retrieveOrderUseCase;

    @MockBean
    private ProcessOrderUseCase processOrderUseCase;

    @MockBean
    private SearchOrderUseCase searchOrderUseCase;

    @MockBean
    private PermissionRepository permissionRepository;

    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Test
    public void whenPostRequestToAddOrder_thenCorrectResponse() throws Exception {
        OrderRequest orderRequest = new OrderRequest()
            .addItemsItem(new OrderItemRequest()
                .productId(new ProductID().getId())
                .quantity(1));

        when(createOrderUseCase.createOrder(any(CreateOrderCommand.class)))
            .thenReturn(OrderMock.generateBasic());

        mockMvc.perform(post("/orders")
                .with(jwt().authorities(new SimpleGrantedAuthority("orders:add")))
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.ticketId").exists());
    }

    @Test
    public void whenDeleteRequestToCancelOrder_thenCorrectResponse() throws Exception {
        doNothing().when(cancelOrderUseCase).cancelOrder(any());

        mockMvc.perform(delete("/orders/{orderId}", "12345678")
                .with(jwt().authorities(new SimpleGrantedAuthority("orders:cancel")))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void whenGetRequestToOrderById_thenCorrectResponse() throws Exception {
        Order order = OrderMock.generateBasic();
        when(retrieveOrderUseCase.getByTicketId(any()))
            .thenReturn(order);

        mockMvc.perform(get("/orders/{orderId}", order.getTicketId())
                .with(jwt().authorities(new SimpleGrantedAuthority("orders:view")))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ticketId").value(order.getTicketId()));
    }

    @Test
    public void whenPostRequestToChangeOrderStatus_thenCorrectResponse() throws Exception {
        doNothing().when(processOrderUseCase).processOrder(any());

        mockMvc.perform(post("/orders/{orderId}/status/{status}",
                "12345678", "PREPARATION")
                .with(jwt().authorities(new SimpleGrantedAuthority("orders:update")))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetRequestToFindOrdersByStatus_thenCorrectResponse() throws Exception {
        Order order = OrderMock.generateBasic();
        when(searchOrderUseCase.searchBy(any()))
            .thenReturn(List.of(order));

        mockMvc.perform(get("/orders")
                .with(jwt().authorities(new SimpleGrantedAuthority("orders:list")))
                .param("category", "burger"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ticketId").value(order.getTicketId()));
    }
}
