package com.gabriel.orders.core.application.usecase;

import com.gabriel.common.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    private final MenuCheckUseCase menuCheckUseCase;

    public CreateOrderUseCase(OrderRepository orderRepository,
                              OrderPublisher orderPublisher,
                              MenuCheckUseCase menuCheckUseCase) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
        this.menuCheckUseCase = menuCheckUseCase;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        try {
            Product sampleProduct = new Product(new ProductID(), "Teu cu", 10.20);
            OrderItem sampleOrder = new OrderItem(sampleProduct);
            Order newOrder = new Order(List.of(sampleOrder));

            menuCheckUseCase.verifyMenu(newOrder);


            orderRepository.saveOrder(newOrder);
            orderPublisher.orderCreated(new OrderCreatedEvent(newOrder));
            return newOrder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
