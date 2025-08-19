package com.gabriel.orders.core.application.exception;

public class OrderApplicationException extends RuntimeException {

    private final OrderApplicationError type;

    public OrderApplicationException(String message, OrderApplicationError type) {
        super(message);
        this.type = type;
    }

    public OrderApplicationError getType() {
        return type;
    }
}
