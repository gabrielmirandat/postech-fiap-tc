package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.AggregateRoot;
import com.gabriel.core.domain.model.Address;
import com.gabriel.core.domain.model.CPF;
import com.gabriel.core.domain.model.Notification;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.OrderID;
import com.gabriel.orders.core.domain.exception.OrderDomainError;
import com.gabriel.orders.core.domain.exception.OrderDomainException;
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

    /**
     * Constructor for Jackson deserialization.
     */
    @JsonCreator
    Order(@JsonProperty("orderId") OrderID orderId, @JsonProperty("items") List<OrderItem> items,
          @JsonProperty("customer") CPF customer, @JsonProperty("shippingAddress") Address shippingAddress,
          @JsonProperty("notification") Notification additionalNotification, @JsonProperty("price") Price price,
          @JsonProperty("ticketId") String ticketId, @JsonProperty("status") OrderStatus status,
          @JsonProperty("creationTimestamp") Instant createdAt, @JsonProperty("updateTimestamp") Instant updatedAt) {
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
            throw new ApplicationException("Error deserializing order", ApplicationError.APP_OO3);
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

    public void promote(OrderStatus toStatus) {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order is already finished and cant be promoted", OrderDomainError.ORD_001);
        }

        if (toStatus == OrderStatus.CREATED) {
            throw new OrderDomainException("Order can't be promoted to created status", OrderDomainError.ORD_001);
        }

        switch (toStatus) {
            case PREPARATION -> prepare_order();
            case PACKAGING -> package_order();
            case PICKUP -> pickup_order();
            case DELIVERY -> deliver_order();
            case COMPLETED -> finish_order();
        }
    }

    // TODO: add methods for redoing order
    public void rollback() {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order is already finished and cant be rolled back", OrderDomainError.ORD_001);
        }

        if (status == OrderStatus.CREATED) {
            throw new OrderDomainException("Order did not started preparation so cant be rolled back", OrderDomainError.ORD_001);
        }

        switch (status) {
            case PREPARATION -> this.status = OrderStatus.CREATED;
            case PACKAGING -> this.status = OrderStatus.PREPARATION;
            case PICKUP -> this.status = OrderStatus.PACKAGING;
            case DELIVERY -> this.status = OrderStatus.PICKUP;
        }
    }

    private void prepare_order() {
        if (status != OrderStatus.CREATED) {
            throw new OrderDomainException("Order must be initiated to be prepared", OrderDomainError.ORD_001);
        }

        this.status = OrderStatus.PREPARATION;
    }

    private void package_order() {
        if (status != OrderStatus.PREPARATION) {
            throw new OrderDomainException("Order must be prepared to be package", OrderDomainError.ORD_001);
        }

        this.status = OrderStatus.PACKAGING;
    }

    private void pickup_order() {
        if (status != OrderStatus.PACKAGING) {
            throw new OrderDomainException("Order must be packaged to be pickup", OrderDomainError.ORD_001);
        }

        this.status = OrderStatus.PICKUP;
    }

    private void deliver_order() {
        if (status != OrderStatus.PICKUP) {
            throw new OrderDomainException("Order must be in balcony to be delivered", OrderDomainError.ORD_001);
        }

        if (shippingAddress == null) {
            throw new OrderDomainException("Order must have an shipping address to be delivered", OrderDomainError.ORD_002);
        }

        this.status = OrderStatus.DELIVERY;
    }

    private void finish_order() {
        if (status != OrderStatus.PICKUP && status != OrderStatus.DELIVERY) {
            throw new OrderDomainException("Order must be in balcony or in delivery to be finished", OrderDomainError.ORD_001);
        }

        this.status = OrderStatus.COMPLETED;
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("Error serializing order", ApplicationError.APP_OO3);
        }
    }
}
