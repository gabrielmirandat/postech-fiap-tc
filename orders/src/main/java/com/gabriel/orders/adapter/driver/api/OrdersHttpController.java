package com.gabriel.orders.adapter.driver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.command.CancelOrderCommand;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.command.ProcessOrderCommand;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.application.query.SearchByOrderStatusQuery;
import com.gabriel.orders.core.application.usecase.*;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.TicketId;
import com.gabriel.specs.orders.OrdersApi;
import com.gabriel.specs.orders.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class OrdersHttpController implements OrdersApi {

    private final CreateOrderUseCase createOrderUseCase;

    private final CancelOrderUseCase cancelOrderUseCase;

    private final RetrieveOrderUseCase retrieveOrderUseCase;

    private final ProcessOrderUseCase processOrderUseCase;

    private final SearchOrderUseCase searchOrderUseCase;

    public OrdersHttpController(CreateOrderUseCase createOrderUseCase,
                                CancelOrderUseCase cancelOrderUseCase,
                                RetrieveOrderUseCase retrieveOrderUseCase,
                                ProcessOrderUseCase processOrderUseCase,
                                SearchOrderUseCase searchOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.retrieveOrderUseCase = retrieveOrderUseCase;
        this.processOrderUseCase = processOrderUseCase;
        this.searchOrderUseCase = searchOrderUseCase;
    }

    @PreAuthorize("hasAuthority('orders:add')")
    public ResponseEntity<OrderCreated> addOrder(OrderRequest orderRequest) throws JsonProcessingException {
        CreateOrderCommand command = OrderMapper.toCommand(orderRequest);
        Order newOrder = createOrderUseCase.createOrder(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newOrder.getTicketId())
            .toUri();

        return ResponseEntity.created(location).body(new OrderCreated(newOrder.getTicketId()));
    }

    @PreAuthorize("hasAuthority('orders:cancel')")
    public ResponseEntity<String> cancelOrder(String orderId) throws Exception {
        cancelOrderUseCase.cancelOrder(new CancelOrderCommand(new TicketId(orderId)));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('orders:view')")
    public ResponseEntity<OrderResponse> getOrderById(String orderId) {
        Order currentOrder = retrieveOrderUseCase.getByTicketId(new GetByTicketOrderQuery(new TicketId(orderId)));
        return ResponseEntity.ok(OrderMapper.toResponse(currentOrder));
    }

    @PreAuthorize("hasAuthority('orders:update')")
    public ResponseEntity<OrderUpdated> changeOrderStatus(String id, OrderStatusDTO status) {
        processOrderUseCase.processOrder(
            new ProcessOrderCommand(new TicketId(id), OrderStatus.valueOf(status.name().toUpperCase())));
        return ResponseEntity.ok(new OrderUpdated(id, status));
    }

    @PreAuthorize("hasAuthority('orders:list')")
    public ResponseEntity<List<OrderResponse>> findOrdersByQuery(Optional<OrderStatusDTO> status) {
        List<Order> orders = searchOrderUseCase.searchBy(
            new SearchByOrderStatusQuery(
                status.map(orderStatusDTO -> OrderStatus.valueOf(orderStatusDTO.name().toUpperCase()))
                    .orElse(null)));
        return ResponseEntity.ok(OrderMapper.toResponseList(orders));
    }


}
