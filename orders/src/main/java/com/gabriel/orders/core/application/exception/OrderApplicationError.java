package com.gabriel.orders.core.application.exception;

public enum OrderApplicationError {

    ORD_100("ORD_100 - INVALID PRODUCT"),

    ORD_101("ORD_101 - INVALID EXTRA");

    final String message;
    
    OrderApplicationError(String message) {
        this.message = message;
    }
}
