package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.AggregateRoot;
import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.entities.enums.OrderStatus;
import com.gabriel.orders.core.domain.valueobjects.*;
import com.gabriel.orders.core.domain.valueobjects.ids.OrderID;
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
    private Price price = null;

    @Valid
    private String ticketId = null;

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
        // reduce
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

    // TODO: add methods for redoing order
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

    public void prepare_order() {
        if (status != OrderStatus.CREATED) {
            throw new DomainException("Order must be initiated to be prepared");
        }

        promote();
    }

    public void package_order() {
        if (status != OrderStatus.PREPARATION) {
            throw new DomainException("Order must be prepared to be package");
        }

        promote();
    }

    public void pickup_order() {
        if (status != OrderStatus.PACKAGING) {
            throw new DomainException("Order must be packaged to be pickup");
        }

        promote();
    }

    public void deliver_order() {
        if (status != OrderStatus.PICKUP) {
            throw new DomainException("Order must be in balcony to be delivered");
        }

        promote();
    }

    public void finish_order() {
        if (status != OrderStatus.PICKUP && status != OrderStatus.DELIVERY) {
            throw new DomainException("Order must be in balcony or in delivery to be finished");
        }

        promote();
    }
}

// o dominio nao se valida, quem valida eh de fora!!!!!
// quem valida regra de negocio eh a aplicacao