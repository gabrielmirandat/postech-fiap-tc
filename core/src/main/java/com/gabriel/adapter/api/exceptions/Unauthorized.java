package com.gabriel.adapter.api.exceptions;

public class Unauthorized extends BaseHttpException {

    public Unauthorized(String message) {
        super(401, message);
    }

    public Unauthorized(String message, String code) {
        super(401, message, code);
    }
}
