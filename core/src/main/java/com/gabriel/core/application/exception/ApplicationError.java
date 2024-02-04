package com.gabriel.core.application.exception;

public enum ApplicationError {

    APP_OO1("APP_OO1 - DATABASE WRITE VALIDATION EXCEPTION"),
    APP_OO2("APP_OO2 - SERIALIZATION EXCEPTION"),
    APP_OO3("APP_OO3 - DESERIALIZATION EXCEPTION");

    final String message;

    ApplicationError(String message) {
        this.message = message;
    }
}
