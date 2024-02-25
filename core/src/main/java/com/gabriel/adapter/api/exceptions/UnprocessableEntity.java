package com.gabriel.adapter.api.exceptions;

import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;

public class UnprocessableEntity extends BaseHttpException {

    public UnprocessableEntity(String message, String code) {
        super(422, message, code);
    }

    public static UnprocessableEntity from(DomainException exception) {
        return new UnprocessableEntity(exception.getMessage(), exception.getType());
    }

    public static UnprocessableEntity from(ApplicationException exception) {
        return new UnprocessableEntity(exception.getMessage(), exception.getType());
    }
}
