package com.gabriel.adapter.api.exceptions;

public class InternalServerError extends BaseHttpException {

    public InternalServerError() {
        super(500, "Internal Server Error");
    }

    public static InternalServerError create() {
        return new InternalServerError();
    }
}
