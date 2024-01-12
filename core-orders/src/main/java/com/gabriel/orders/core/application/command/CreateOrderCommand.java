package com.gabriel.orders.core.application.command;

import com.gabriel.common.core.domain.model.Address;
import com.gabriel.common.core.domain.model.CPF;
import com.gabriel.common.core.domain.model.Notification;
import com.gabriel.orders.core.domain.model.OrderItemRef;

import java.util.List;

public record CreateOrderCommand(CPF customer, Address shippingAddress, Notification notification,
                                 List<OrderItemRef> items) {
}
