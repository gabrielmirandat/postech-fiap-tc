package com.gabriel.orders.core.application.usecases;

import com.gabriel.orders.core.application.queries.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.ports.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

public class RetrieveOrderUseCase {

    private final OrderRepository orderRepository;

    public RetrieveOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Order getByTicketOrder(GetByTicketOrderQuery query) {
        return orderRepository.getByTicket(query.ticket());
    }
}

