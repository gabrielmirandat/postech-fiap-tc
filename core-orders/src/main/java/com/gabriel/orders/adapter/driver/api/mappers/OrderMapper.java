package com.gabriel.orders.adapter.driver.api.mappers;

import com.gabriel.orders.adapter.driver.api.controllers.models.OrderRequest;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderResponse;
import com.gabriel.orders.core.domain.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toDomain(OrderRequest request) {
        return null;

    }

    public OrderResponse toResponse(Order order) {
        return null;

    }
}
