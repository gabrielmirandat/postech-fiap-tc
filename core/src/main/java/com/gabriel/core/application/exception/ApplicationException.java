package com.gabriel.core.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final String message;
    private final ApplicationError type;

    public ApplicationException(String message, ApplicationError type) {
        this.message = message;
        this.type = type;
    }
}
