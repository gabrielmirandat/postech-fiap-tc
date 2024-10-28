package com.gabriel.core.domain.exception;

public class DomainException extends RuntimeException {

    private final String message;
    private final DomainError type;

    public DomainException(String message, DomainError type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type.message;
    }
}
