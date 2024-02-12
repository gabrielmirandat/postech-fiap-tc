package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.query.SearchByOrderStatusQuery;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchOrderUseCase {

    private final OrderRepository orderRepository;

    public SearchOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> searchBy(SearchByOrderStatusQuery query) {
        return orderRepository.searchBy(new OrderSearchParameters(query.status()));
    }
}
