package com.gabriel.orders.core.domain.exception;

public enum OrderDomainError {

    ORD_001("ORD_001 - INVALID ORDER STATUS CHANGE"),
    ORD_002("ORD_002 - TRYING TO DELIVER A ORDER WITHOUT SHIPPING ADDRESS");

    final String message;

    OrderDomainError(String message) {
        this.message = message;
    }
}
