package com.gabriel.orders.core.application.usecases.impl;

import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.application.services.MenuCheckService;
import com.gabriel.orders.core.application.usecases.CreateOrderUseCase;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.events.OrderEvent;
import com.gabriel.orders.core.domain.events.enums.Operation;
import com.gabriel.orders.core.domain.repositories.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private MenuCheckService menuCheckService;

    public CreateOrderUseCaseImpl(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    // validar

    @Override
    @Transactional
    public Order createOrder(CreateOrderCommand command) {
        return null;
    }
}
