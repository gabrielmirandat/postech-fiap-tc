package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.ports.models.OrderSearchParameters;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    List<Order> search(OrderSearchParameters parameters);
}
