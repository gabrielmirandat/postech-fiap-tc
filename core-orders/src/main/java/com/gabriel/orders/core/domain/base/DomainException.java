package com.gabriel.orders.core.domain.base;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
