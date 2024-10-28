package com.gabriel.core.application.exception;

public class ApplicationException extends RuntimeException {

    private final String message;
    private final ApplicationError type;

    public ApplicationException(String message, ApplicationError type) {
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
