package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

