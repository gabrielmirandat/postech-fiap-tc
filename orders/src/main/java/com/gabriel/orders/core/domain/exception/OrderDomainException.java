package com.gabriel.orders.core.domain.exception;

import com.gabriel.core.domain.exception.DomainException;

public class OrderDomainException extends DomainException {

    private final OrderDomainError type;

    public OrderDomainException(String message, OrderDomainError type) {
        super(message, null);
        this.type = type;
    }

    @Override
    public String getType() {
        return type.name();
    }
}
