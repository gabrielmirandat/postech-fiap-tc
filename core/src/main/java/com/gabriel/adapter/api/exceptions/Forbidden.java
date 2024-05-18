package com.gabriel.adapter.api.exceptions;

public class Forbidden extends BaseHttpException {

    public Forbidden(String message) {
        super(403, message);
    }

    public Forbidden(String message, String code) {
        super(403, message, code);
    }

    public static Forbidden from(Exception exception) {
        return new Forbidden(exception.getMessage());
    }
}
