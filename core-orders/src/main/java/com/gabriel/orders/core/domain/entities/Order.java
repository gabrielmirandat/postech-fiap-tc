package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.*;
import jakarta.validation.Valid;

import java.util.List;

public class Order extends Entity {

    @Valid
    private OrderID orderId;

    private List<Product> products;

    @Valid
    private Address shippingAddress;

    @Valid
    private Notification additionalNotification;

    @Valid
    private Price price;

    @Valid
    private String ticketId;
}
