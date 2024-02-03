package com.gabriel.adapter.api.exceptions;

public class PreconditionFailed extends BaseHttpException {

    public PreconditionFailed(String message, String code) {
        super(412, message, code);
    }
}
