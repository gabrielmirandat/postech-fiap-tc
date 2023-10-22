package com.gabriel.orders.adapter.driven.infra.repositories;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.repositories.OrderRepository;
import com.gabriel.orders.core.domain.repositories.models.OrderSearchParameters;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderJpaRepository implements OrderRepository {

    @Override
    public Order save(Order order) {
        return order;
    }

    @Override
    public List<Order> search(OrderSearchParameters parameters) {
        return null;
    }
    // use Spring Data JPA or JDBC to save the order

}
