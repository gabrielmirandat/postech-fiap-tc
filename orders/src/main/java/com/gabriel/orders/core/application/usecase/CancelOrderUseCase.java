package com.gabriel.orders.core.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.core.application.command.CancelOrderCommand;
import com.gabriel.orders.core.domain.event.OrderDeletedEvent;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public CancelOrderUseCase(OrderRepository orderRepository, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
    }

    @Transactional
    public void cancelOrder(CancelOrderCommand command) throws JsonProcessingException {
        Order toUpdateOrder = orderRepository.getByTicket(command.ticketId().getId());
        toUpdateOrder.cancel_order();
        orderRepository.updateOrder(toUpdateOrder);
        orderPublisher.orderCanceled(new OrderDeletedEvent(toUpdateOrder.getTicketId()));
    }
}
