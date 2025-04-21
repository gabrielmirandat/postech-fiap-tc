package com.gabriel.orders.core.application.command;

import com.gabriel.model.Address;
import com.gabriel.model.Cpf;
import com.gabriel.model.Contact;
import com.gabriel.orders.core.domain.model.OrderItemRef;

import java.util.List;

public record CreateOrderCommand(Cpf customer, Address shippingAddress, Contact notification,
                                 List<OrderItemRef> items) {
}
