package com.gabriel.orders.core.application.command;

import com.gabriel.domain.model.Address;
import com.gabriel.domain.model.CPF;
import com.gabriel.domain.model.Notification;
import com.gabriel.orders.core.domain.model.OrderItemRef;

import java.util.List;

public record CreateOrderCommand(CPF customer, Address shippingAddress, Notification notification,
                                 List<OrderItemRef> items) {
}
