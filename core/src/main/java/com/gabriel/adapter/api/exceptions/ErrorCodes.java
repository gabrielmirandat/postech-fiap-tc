package com.gabriel.adapter.api.exceptions;

public enum ErrorCodes {
    DOM_001("DOM_001"); // Domain validation error

    private final String code;

    ErrorCodes(String code) {
        this.code = code;
    }
}
