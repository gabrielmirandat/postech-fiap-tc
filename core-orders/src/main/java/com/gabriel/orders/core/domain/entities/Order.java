package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.AggregateRoot;
import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.entities.enums.OrderStatus;
import com.gabriel.orders.core.domain.valueobjects.*;
import jakarta.validation.Valid;

import java.util.List;

public class Order extends AggregateRoot {

    @Valid
    private final OrderID orderId;

    private final List<OrderItem> items;

    @Valid
    private final Address shippingAddress;

    @Valid
    private final Notification additionalNotification;

    @Valid
    private final Price price;

    @Valid
    private String ticketId;

    private OrderStatus status;

    public Order(List<OrderItem> items, Address shippingAddress, Notification additionalNotification) {
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.additionalNotification = additionalNotification;

        this.orderId = new OrderID();
        this.status = OrderStatus.CREATED;
    }

    public void generateTicket() {
        this.ticketId = orderId.getId().split("-")[0];
    }

    public void calculatePrice() {
        // somar precos dos produtos com precos
    }

    private void promote() {
        if (status ==  OrderStatus.COMPLETED) {
            throw new DomainException("Order is already finished and cant be promoted");
        }

        switch (status) {
            case CREATED -> this.status = OrderStatus.PREPARATION;
            case PREPARATION -> this.status = OrderStatus.PACKAGING;
            case PACKAGING -> this.status = OrderStatus.PICKUP;
            case PICKUP -> {
                if (shippingAddress != null) {
                    this.status = OrderStatus.DELIVERY;
                } else {
                    this.status = OrderStatus.COMPLETED;
                }
            }
            case DELIVERY -> this.status = OrderStatus.COMPLETED;
        }
    }

    private void rollback() {
        if (status ==  OrderStatus.COMPLETED) {
            throw new DomainException("Order is already finished and cant be back");
        }

        switch (status) {
            case PACKAGING -> this.status = OrderStatus.PREPARATION;
            case PICKUP -> this.status = OrderStatus.PACKAGING;
            case DELIVERY -> this.status = OrderStatus.PICKUP;
        }
    }

    public void prepare() {
        // assert is in CREATED
    }
}

// o dominio nao se valida, quem valida eh de fora!!!!!
// quem valida regra de negocio eh a aplicacao