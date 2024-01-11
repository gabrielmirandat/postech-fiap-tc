package com.gabriel.orders.core.application.usecases;

import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.entities.OrderItem;
import com.gabriel.orders.core.domain.events.OrderCreatedEvent;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import com.gabriel.orders.core.domain.ports.OrderRepository;
import com.gabriel.orders.core.domain.valueobjects.Product;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public CreateOrderUseCase(OrderRepository orderRepository,
                              OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        Product sampleProduct = new Product(new ProductID(), "Teu cu", 10.20);
        OrderItem sampleOrder = new OrderItem(sampleProduct);
        Order newOrder = new Order(List.of(sampleOrder));

        orderRepository.saveOrder(newOrder);
        orderPublisher.orderCreated(new OrderCreatedEvent(newOrder));
        return newOrder;
    }
}
