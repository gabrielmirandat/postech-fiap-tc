package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.ApplicationCode;
import com.gabriel.model.ApplicationException;
import com.gabriel.model.Address;
import com.gabriel.model.Cpf;
import com.gabriel.model.Contact;
import com.gabriel.model.Price;
import com.gabriel.model.OrderId;
import com.gabriel.model.Model;
import com.gabriel.orders.core.domain.exception.OrderDomainError;
import com.gabriel.orders.core.domain.exception.OrderDomainException;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class Order {

    private final OrderId orderId;

    private final List<OrderItem> items;

    private Price price;

    private String ticketId;

    private OrderStatus status;

    private Cpf customer;

    private Address shippingAddress;

    private Contact contact;

    private Instant creationTimestamp;

    private Instant updateTimestamp;

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    // Private constructor for internal use
    private Order(OrderId orderId, List<OrderItem> items, Cpf customer, Address shippingAddress,
                  Contact additionalContact, Instant creationTimestamp, Instant updateTimestamp) {
        this.orderId = orderId;
        this.items = items;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.contact = additionalContact;
        this.creationTimestamp = creationTimestamp;
        this.updateTimestamp = updateTimestamp;
        initialize();
    }

    // Factory methods que sempre validam
    public static Order create(List<OrderItem> items) {
        OrderId validatedOrderId = (OrderId) Model.validate(OrderId.newBuilder().setValue(generateOrderId()).build());
        return new Order(validatedOrderId, items, null, null, null, Instant.now(), Instant.now());
    }

    public static Order create(List<OrderItem> items, Cpf customer, Address shippingAddress,
                              Contact additionalContact) {
        OrderId validatedOrderId = (OrderId) Model.validate(OrderId.newBuilder().setValue(generateOrderId()).build());
        return new Order(validatedOrderId, items, customer, shippingAddress, additionalContact, Instant.now(), Instant.now());
    }

    // Constructor for Jackson deserialization (no validation to avoid duplication)
    @JsonCreator
    public static Order fromJson(@JsonProperty("orderId") OrderId orderId, @JsonProperty("items") List<OrderItem> items,
                                 @JsonProperty("customer") Cpf customer, @JsonProperty("shippingAddress") Address shippingAddress,
                                 @JsonProperty("contact") Contact additionalContact, @JsonProperty("price") Price price,
                                 @JsonProperty("ticketId") String ticketId, @JsonProperty("status") OrderStatus status,
                                 @JsonProperty("creationTimestamp") Instant createdAt, @JsonProperty("updateTimestamp") Instant updatedAt) {
        Order order = new Order(orderId, items, customer, shippingAddress, additionalContact, createdAt, updatedAt);
        order.price = price;
        order.ticketId = ticketId;
        order.status = status;
        return order;
    }

    public static Order copy(OrderId orderId, List<OrderItem> items, Cpf customer,
                             Address shippingAddress, Contact additionalContact,
                             Price price, String ticketId, OrderStatus status,
                             Instant createdAt, Instant updatedAt) {
        Order order = new Order(orderId, items, customer, shippingAddress, additionalContact, createdAt, updatedAt);
        order.price = price;
        order.ticketId = ticketId;
        order.status = status;
        return order;
    }

    public static Order deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Order.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing order", e);
        }
    }

    private void initialize() {
        this.status = OrderStatus.CREATED;
        this.generateTicket();
        this.calculatePrice();
    }

    private void generateTicket() {
        ticketId = orderId.getValue().split("-")[0];
    }

    private void calculatePrice() {
        Double productsTotalPrice = items.parallelStream()
            .map(item -> item.getProduct().getPrice().getValue())
            .reduce(0.0, Double::sum);

        Double extrasTotalPrice = items.stream()
            .flatMap(item -> item.getExtras().stream())
            .map(extra -> extra.getPrice().getValue())
            .reduce(0.0, Double::sum);

        price = Price.newBuilder().setValue(productsTotalPrice + extrasTotalPrice).build();
    }

    public void promote(OrderStatus toStatus) {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order is already finished and cant be promoted", OrderDomainError.ORD_001);
        }

        if (status == OrderStatus.CANCELED) {
            throw new OrderDomainException("Order is already canceled and cant be promoted", OrderDomainError.ORD_001);
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
            case CANCELED -> throw new UnsupportedOperationException("Unimplemented case: " + toStatus);
        }
    }

    // add methods for redoing order
    public void rollback() {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order is already finished and cant be rolled back", OrderDomainError.ORD_001);
        }

        if (status == OrderStatus.CANCELED) {
            throw new OrderDomainException("Order is already canceled and cant be rolled back", OrderDomainError.ORD_001);
        }

        if (status == OrderStatus.CREATED) {
            throw new OrderDomainException("Order did not started preparation so cant be rolled back", OrderDomainError.ORD_001);
        }

        switch (status) {
            case PREPARATION -> this.status = OrderStatus.CREATED;
            case PACKAGING -> this.status = OrderStatus.PREPARATION;
            case PICKUP -> this.status = OrderStatus.PACKAGING;
            case DELIVERY -> this.status = OrderStatus.PICKUP;
            case CANCELED -> throw new UnsupportedOperationException("Unimplemented case: " + status);
            case COMPLETED -> throw new UnsupportedOperationException("Unimplemented case: " + status);
            case CREATED -> throw new UnsupportedOperationException("Unimplemented case: " + status);
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
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

    public void cancel_order() {
        if (status == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order is already finished and cant be canceled", OrderDomainError.ORD_001);
        }

        if (status == OrderStatus.CANCELED) {
            throw new OrderDomainException("Order is already canceled", OrderDomainError.ORD_001);
        }

        this.status = OrderStatus.CANCELED;
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing order", e);
        }
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Price getPrice() {
        return price;
    }

    public String getTicketId() {
        return ticketId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Cpf getCustomer() {
        return customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public Contact getContact() {
        return contact;
    }

    private static String generateOrderId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8) + "-ORDR-" + 
               java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
