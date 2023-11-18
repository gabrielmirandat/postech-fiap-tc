package com.gabriel.orders.adapter.driver.api;

import com.gabriel.orders.adapter.driver.api.controllers.OrdersApi;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderCreated;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderRequest;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderResponse;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderStatusDTO;
import com.gabriel.orders.adapter.driver.api.mappers.OrderMapper;
import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.application.usecases.CreateOrderUseCase;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.entities.Product;
import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class OrdersHttpController implements OrdersApi {

    private final CreateOrderUseCase createOrderUseCase;

    private final OrderMapper orderMapper;

    private final OrderPublisher orderPublisher;

    public OrdersHttpController(CreateOrderUseCase createOrderUseCase, OrderMapper orderMapper, OrderPublisher orderPublisher) {
        this.createOrderUseCase = createOrderUseCase;
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
    public ResponseEntity<Void> changeOrderStatus(String orderId, String status) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> findOrdersByQuery(Optional<OrderStatusDTO> status) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId) throws Exception {
        ProductID id = new ProductID();

        orderPublisher.productCreated(new MenuAddedEvent(new Product(id, "bla", 10.20)));
        return null;
    }
}
