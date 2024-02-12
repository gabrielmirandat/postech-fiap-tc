package com.gabriel.orders.core.application.exception;

import com.gabriel.core.application.exception.ApplicationException;

public class OrderApplicationException extends ApplicationException {

    private final OrderApplicationError type;

    public OrderApplicationException(String message, OrderApplicationError type) {
        super(message, null);
        this.type = type;
    }

    @Override
    public String getType() {
        return type.message;
    }
}
