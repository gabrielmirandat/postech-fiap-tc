package com.gabriel.orders.core.application.usecases;

import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.domain.entities.Order;

public interface CreateOrderUseCase {

    Order createOrder(CreateOrderCommand command);
}
