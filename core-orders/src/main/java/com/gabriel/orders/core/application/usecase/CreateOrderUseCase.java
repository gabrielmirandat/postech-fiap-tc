package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;
    private final VerifyMenuUseCase verifyMenuUseCase;
    private final MenuRepository menuRepository;
    private final OrderMapper orderMapper;

    public CreateOrderUseCase(OrderRepository orderRepository,
                              OrderPublisher orderPublisher,
                              VerifyMenuUseCase verifyMenuUseCase,
                              MenuRepository menuRepository,
                              OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
        this.verifyMenuUseCase = verifyMenuUseCase;
        this.menuRepository = menuRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        try {
            verifyMenuUseCase.validate(command);
            Order newOrder = orderMapper.toOrder(command, menuRepository);

            orderRepository.saveOrder(newOrder);
            orderPublisher.orderCreated(new OrderCreatedEvent(newOrder));
            return newOrder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
