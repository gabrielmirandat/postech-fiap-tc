package com.gabriel.orders.adapter.driver.api;

import com.gabriel.orders.adapter.driver.api.controllers.OrdersApi;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderCreated;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderRequest;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderResponse;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderStatusDTO;
import com.gabriel.orders.adapter.driver.api.mappers.OrderMapper;
import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.application.queries.GetByTicketOrderQuery;
import com.gabriel.orders.core.application.usecases.CreateOrderUseCase;
import com.gabriel.orders.core.application.usecases.RetrieveOrderUseCase;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class OrdersHttpController implements OrdersApi {

    private final CreateOrderUseCase createOrderUseCase;

    private final RetrieveOrderUseCase retrieveOrderUseCase;

    private final OrderMapper orderMapper;

    private final OrderPublisher orderPublisher;

    public OrdersHttpController(CreateOrderUseCase createOrderUseCase, RetrieveOrderUseCase retrieveOrderUseCase, OrderMapper orderMapper, OrderPublisher orderPublisher) {
        this.createOrderUseCase = createOrderUseCase;
        this.retrieveOrderUseCase = retrieveOrderUseCase;
        this.orderMapper = orderMapper;
        this.orderPublisher = orderPublisher;
    }

    @Override
    public ResponseEntity<OrderCreated> addOrder(OrderRequest orderRequest) throws Exception {
        CreateOrderCommand command = orderMapper.toCommand(orderRequest);
        Order newOrder = createOrderUseCase.createOrder(command);
        return ResponseEntity.ok(new OrderCreated(newOrder.getTicketId()));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId) throws Exception {
        GetByTicketOrderQuery query = orderMapper.toQuery(orderId);
        Order existentOrder = retrieveOrderUseCase.getByTicketOrder(query);
        return ResponseEntity.ok(orderMapper.toResponse(existentOrder));
    }

    @Override
    public ResponseEntity<Void> changeOrderStatus(String orderId, String status) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> findOrdersByQuery(Optional<OrderStatusDTO> status) throws Exception {
        return null;
    }
}
