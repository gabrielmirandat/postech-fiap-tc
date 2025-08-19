package com.gabriel.orders.core.domain.exception;

import com.gabriel.model.DomainException;

public class OrderDomainException extends RuntimeException {

    private final OrderDomainError type;

    public OrderDomainException(String message, OrderDomainError type) {
        super(message);
        this.type = type;
    }

    public OrderDomainError getType() {
        return type;
    }
}
