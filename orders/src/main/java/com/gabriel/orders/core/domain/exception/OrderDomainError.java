package com.gabriel.orders.core.domain.exception;

public enum OrderDomainError {

    ORD_001, // Invalid order status change
    ORD_002 // Trying to deliver a order without shipping address
}
