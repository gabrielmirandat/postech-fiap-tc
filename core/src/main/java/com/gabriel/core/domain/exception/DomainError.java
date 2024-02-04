package com.gabriel.core.domain.exception;

public enum DomainError {
    
    DOM_OO1("DOM_OO1 - INVALID DOMAIN ID CONVERSION"),
    DOM_OO2("DOM_OO2 - OTHER");

    final String message;

    DomainError(String message) {
        this.message = message;
    }
}
