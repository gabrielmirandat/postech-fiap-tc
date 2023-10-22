package com.gabriel.orders.core.domain.repositories;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.repositories.models.OrderSearchParameters;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    List<Order> search(OrderSearchParameters parameters);
}
