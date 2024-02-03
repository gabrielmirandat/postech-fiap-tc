package com.gabriel.adapter.api.exceptions;

public class NotFound extends BaseHttpException {

    public NotFound(String message) {
        super(404, message);
    }

    public NotFound(String message, String code) {
        super(404, message, code);
    }
}
