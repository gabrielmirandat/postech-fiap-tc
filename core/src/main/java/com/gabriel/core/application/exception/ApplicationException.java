package com.gabriel.core.application.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    @Getter
    private final String message;
    private final ApplicationError type;

    public ApplicationException(String message, ApplicationError type) {
        this.message = message;
        this.type = type;
    }

    public String getType() {
        return type.message;
    }
}
