package com.gabriel.orders.core.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public CreateOrderUseCase(OrderRepository orderRepository,
                              OrderPublisher orderPublisher,
                              VerifyMenuUseCase verifyMenuUseCase,
                              MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
        this.verifyMenuUseCase = verifyMenuUseCase;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderCommand command) throws JsonProcessingException {
        verifyMenuUseCase.validate(command);
        Order newOrder = OrderMapper.toOrder(command, menuRepository);

        orderRepository.saveOrder(newOrder);
        orderPublisher.orderCreated(new OrderCreatedEvent(newOrder));
        return newOrder;
    }
}
