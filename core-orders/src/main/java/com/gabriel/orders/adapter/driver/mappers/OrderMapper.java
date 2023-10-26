package com.gabriel.orders.adapter.driver.mappers;

import com.gabriel.orders.adapter.driver.api.controllers.models.OrderRequest;
import com.gabriel.orders.core.domain.entities.Order;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final Mapper mapper;

    public OrderMapper(Mapper mapper) {
        this.mapper = mapper;


    }

    public OrderRequest toDomain(Order order ) {
        return null;
    }

    public Order toRequest(OrderRequest request) {
        return null;
    }
}