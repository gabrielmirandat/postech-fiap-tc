package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.command.ProcessOrderCommand;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class ProcessOrderUseCase {

    private final OrderRepository orderRepository;

    public ProcessOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void processOrder(ProcessOrderCommand command) {
        Order toUpdateOrder = orderRepository.getByTicket(command.ticketId());
        toUpdateOrder.promote(command.status());
        orderRepository.updateOrder(toUpdateOrder);
    }
}
