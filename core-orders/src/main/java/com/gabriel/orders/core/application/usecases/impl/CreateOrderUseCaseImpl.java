package com.gabriel.orders.core.application.usecases.impl;

import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.application.usecases.CreateOrderUseCase;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.entities.OrderItem;
import com.gabriel.orders.core.domain.entities.Product;
import com.gabriel.orders.core.domain.ports.OrderPublisher;
import com.gabriel.orders.core.domain.ports.OrderRepository;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public CreateOrderUseCaseImpl(OrderRepository orderRepository,
                                  OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        Product sampleProduct = new Product(new ProductID(), "Teu cu", 10.20);
        OrderItem sampleOrder = new OrderItem(sampleProduct);
        Order newOrder = new Order(List.of(sampleOrder));

        orderRepository.saveOrder(newOrder);
        return newOrder;
    }
}
