package com.gabriel.adapter.api.exceptions;

import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.exception.DomainException;

public class PreconditionFailed extends BaseHttpException {

    public PreconditionFailed(String message, String code) {
        super(412, message, code);
    }

    public static PreconditionFailed from(DomainException exception) {
        return new PreconditionFailed(exception.getMessage(), exception.getType());
    }

    public static PreconditionFailed from(ApplicationException exception) {
        return new PreconditionFailed(exception.getMessage(), exception.getType());
    }
}
