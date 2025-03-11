package com.gabriel.model;

import build.buf.protovalidate.Validator;
import build.buf.protovalidate.exceptions.ValidationException;
import build.buf.protovalidate.ValidationResult;

import java.util.stream.Collectors;

import com.google.protobuf.Message;

public class Model {

    public static class Exception extends RuntimeException {
        int status;
        String code;
    
        public Exception(DomainException exception) {
            super(exception.getDetails()); // Set the message for RuntimeException
            this.code = exception.hasCode() ? exception.getCode().name() : null;
        }
    
        public Exception(ApplicationException exception) {
            super(exception.getDetails()); // Set the message for RuntimeException
            this.code = exception.hasCode() ? exception.getCode().name() : null;
        }
    }

    public static Message validate(Message model) {
        try {
            Validator validator = new Validator();
            ValidationResult result = validator.validate(model);

            if (result.getViolations() != null && !result.getViolations().isEmpty()) {
                throw new ValidationException(result.toString());
            }

            return model;
        } catch (ValidationException e) {
            throw new Exception(
                DomainException.newBuilder()
                            .setDetails(e.getMessage())
                            .setCode(DomainCode.DOM_OO1)
                            .build()
            );
        }
    }
} 