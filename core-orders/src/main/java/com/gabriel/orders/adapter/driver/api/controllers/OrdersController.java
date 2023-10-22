package com.gabriel.orders.adapter.driver.api.controllers;

import com.gabriel.orders.adapter.driver.mappers.OrderMapper;
import com.gabriel.orders.core.application.usecases.CreateOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {

    private final CreateOrderUseCase createOrderUseCase;

    private final OrderMapper orderMapper;

    public OrdersController(CreateOrderUseCase createOrderUseCase, OrderMapper orderMapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.orderMapper = orderMapper;
    }
}
