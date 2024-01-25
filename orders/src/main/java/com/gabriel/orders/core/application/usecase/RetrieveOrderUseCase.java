package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class RetrieveOrderUseCase {

    private final OrderRepository orderRepository;

    public RetrieveOrderUseCase(OrderRepository orderRepository,
                                MenuRepository menuRepository,
                                OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
    }


    public Order getByTicketId(GetByTicketOrderQuery query) {
        return orderRepository.getByTicket(query.ticket());
    }
}

