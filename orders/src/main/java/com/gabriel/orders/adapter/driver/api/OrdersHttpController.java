package com.gabriel.orders.adapter.driver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.command.ProcessOrderCommand;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.application.query.SearchByOrderStatusQuery;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.application.usecase.ProcessOrderUseCase;
import com.gabriel.orders.core.application.usecase.RetrieveOrderUseCase;
import com.gabriel.orders.core.application.usecase.SearchOrderUseCase;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.TicketId;
import com.gabriel.specs.orders.OrdersApi;
import com.gabriel.specs.orders.models.OrderCreated;
import com.gabriel.specs.orders.models.OrderRequest;
import com.gabriel.specs.orders.models.OrderResponse;
import com.gabriel.specs.orders.models.OrderStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class OrdersHttpController implements OrdersApi {

    private final CreateOrderUseCase createOrderUseCase;

    private final RetrieveOrderUseCase retrieveOrderUseCase;

    private final ProcessOrderUseCase processOrderUseCase;

    private final SearchOrderUseCase searchOrderUseCase;

    public OrdersHttpController(CreateOrderUseCase createOrderUseCase,
                                RetrieveOrderUseCase retrieveOrderUseCase,
                                ProcessOrderUseCase processOrderUseCase,
                                SearchOrderUseCase searchOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.retrieveOrderUseCase = retrieveOrderUseCase;
        this.processOrderUseCase = processOrderUseCase;
        this.searchOrderUseCase = searchOrderUseCase;
    }

    @Override
    public ResponseEntity<OrderCreated> addOrder(OrderRequest orderRequest) throws JsonProcessingException {
        CreateOrderCommand command = OrderMapper.toCommand(orderRequest);
        Order newOrder = createOrderUseCase.createOrder(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newOrder.getTicketId())
            .toUri();

        return ResponseEntity.created(location).body(new OrderCreated(newOrder.getTicketId()));
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(String orderId) {
        Order currentOrder = retrieveOrderUseCase.getByTicketId(new GetByTicketOrderQuery(new TicketId(orderId)));
        return ResponseEntity.ok(OrderMapper.toResponse(currentOrder));
    }

    @Override
    public ResponseEntity<Void> changeOrderStatus(String orderId, OrderStatusDTO status) throws Exception {
        processOrderUseCase.processOrder(
            new ProcessOrderCommand(orderId, OrderStatus.valueOf(status.name().toUpperCase())));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findOrdersByQuery(Optional<OrderStatusDTO> status) throws Exception {
        List<Order> orders = searchOrderUseCase.searchBy(
            new SearchByOrderStatusQuery(
                status.map(orderStatusDTO -> OrderStatus.valueOf(orderStatusDTO.name().toUpperCase()))
                    .orElse(null)));
        return ResponseEntity.ok(OrderMapper.toResponseList(orders));
    }
}
