package com.gabriel.orders.adapter.driver.api;

import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.application.usecase.RetrieveOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.specs.orders.OrdersApi;
import com.gabriel.specs.orders.models.OrderCreated;
import com.gabriel.specs.orders.models.OrderRequest;
import com.gabriel.specs.orders.models.OrderResponse;
import com.gabriel.specs.orders.models.OrderStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class OrdersHttpController implements OrdersApi {

    private final CreateOrderUseCase createOrderUseCase;

    private final RetrieveOrderUseCase retrieveOrderUseCase;

    private final OrderMapper orderMapper;

    public OrdersHttpController(CreateOrderUseCase createOrderUseCase, RetrieveOrderUseCase retrieveOrderUseCase, OrderMapper orderMapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.retrieveOrderUseCase = retrieveOrderUseCase;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResponseEntity<OrderCreated> addOrder(OrderRequest orderRequest) {
        CreateOrderCommand command = orderMapper.toCommand(orderRequest);
        Order newOrder = createOrderUseCase.createOrder(command);
        return ResponseEntity.ok(new OrderCreated(newOrder.getTicketId()));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId) {
        Order currentOrder = retrieveOrderUseCase.getByTicketId(new GetByTicketOrderQuery(orderId));
        return ResponseEntity.ok(orderMapper.toResponse(currentOrder));
    }

    @Override
    public ResponseEntity<Void> changeOrderStatus(String orderId, String status) {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> findOrdersByQuery(Optional<OrderStatusDTO> status) {
        return null;
    }
}
