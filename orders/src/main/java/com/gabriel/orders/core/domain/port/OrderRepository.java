package com.gabriel.orders.core.domain.port;

import com.gabriel.orders.core.domain.model.Order;

import java.util.List;

public interface OrderRepository {

    Order saveOrder(Order order);

    Order updateOrder(Order newOrder);

    Order getByTicket(String ticketId);

    List<Order> searchBy(OrderSearchParameters parameters);
}
