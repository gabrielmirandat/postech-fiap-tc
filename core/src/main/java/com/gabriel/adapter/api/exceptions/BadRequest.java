package com.gabriel.adapter.api.exceptions;

public class BadRequest extends BaseHttpException {

    public BadRequest(String message) {
        super(400, message);
    }

    public BadRequest(String message, String code) {
        super(400, message, code);
    }
}
