package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.domain.AggregateRoot;
import com.gabriel.domain.DomainException;
import com.gabriel.domain.model.Address;
import com.gabriel.domain.model.CPF;
import com.gabriel.domain.model.Notification;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.OrderID;
import jakarta.validation.Valid;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Getter
public class Order extends AggregateRoot {

    @Valid
    private final OrderID orderId;

    @Valid
    private final List<OrderItem> items;

    @Valid
    private Price price;

    private String ticketId;

    private OrderStatus status;

    @Valid
    private CPF customer;

    @Valid
    private Address shippingAddress;

    @Valid
    private Notification notification;

    public Order(List<OrderItem> items) {
        this.orderId = new OrderID();
        this.items = items;
        initialize();
    }

    public Order(List<OrderItem> items, CPF customer, Address shippingAddress,
                 Notification additionalNotification) {
        this.orderId = new OrderID();
        this.items = items;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.notification = additionalNotification;
        initialize();
    }

    private Order(OrderID orderId, List<OrderItem> items, CPF customer, Address shippingAddress, Notification additionalNotification,
                  Price price, String ticketId, OrderStatus status, Instant createdAt, Instant updatedAt) {
        this.orderId = orderId;
        this.items = items;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.notification = additionalNotification;
        this.price = price;
        this.ticketId = ticketId;
        this.status = status;
        this.creationTimestamp = createdAt;
        this.updateTimestamp = updatedAt;
    }

    public static Order copy(OrderID orderId, List<OrderItem> items, CPF customer,
                             Address shippingAddress, Notification additionalNotification,
                             Price price, String ticketId, OrderStatus status,
                             Instant createdAt, Instant updatedAt) {
        return new Order(orderId, items, customer, shippingAddress, additionalNotification,
            price, ticketId, status, createdAt, updatedAt);
    }

    public static Order deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Order.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing order");
        }
    }

    private void initialize() {
        this.status = OrderStatus.CREATED;
        this.generateTicket();
        this.calculatePrice();
    }

    private void generateTicket() {
        ticketId = orderId.getId().split("-")[0];
    }

    private void calculatePrice() {
        Double productsTotalPrice = items.parallelStream()
            .map(item -> item.getProduct().getPrice().getValue())
            .reduce(0.0, Double::sum);

        Double extrasTotalPrice = items.stream()
            .flatMap(item -> item.getExtras().stream())
            .map(extra -> extra.getPrice().getValue())
            .reduce(0.0, Double::sum);

        price = new Price(productsTotalPrice + extrasTotalPrice);
    }

    private void promote() {
        if (status == OrderStatus.COMPLETED) {
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
        if (status == OrderStatus.COMPLETED) {
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

        if (shippingAddress == null) {
            throw new DomainException("Order must have an shipping address to be delivered");
        }

        promote();
    }

    public void finish_order() {
        if (status != OrderStatus.PICKUP && status != OrderStatus.DELIVERY) {
            throw new DomainException("Order must be in balcony or in delivery to be finished");
        }

        promote();
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing order");
        }
    }
}
