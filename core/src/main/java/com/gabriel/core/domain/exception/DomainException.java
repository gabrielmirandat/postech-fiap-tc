package com.gabriel.core.domain.exception;

import lombok.Getter;

public class DomainException extends RuntimeException {

    @Getter
    private final String message;
    private final DomainError type;

    public DomainException(String message, DomainError type) {
        this.message = message;
        this.type = type;
    }

    public String getType() {
        return type.message;
    }
}
