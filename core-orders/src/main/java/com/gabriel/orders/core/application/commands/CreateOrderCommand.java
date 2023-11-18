package com.gabriel.orders.core.application.commands;

import com.gabriel.orders.core.domain.valueobjects.Address;
import com.gabriel.orders.core.domain.valueobjects.CPF;
import com.gabriel.orders.core.domain.valueobjects.Notification;
import com.gabriel.orders.core.domain.valueobjects.OrderItemRef;

import java.util.List;

public record CreateOrderCommand(CPF customer, Address shippingAddress, Notification notification,
                                 List<OrderItemRef> items) {
}
