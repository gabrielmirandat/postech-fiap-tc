package com.gabriel.core.domain;

import com.gabriel.core.domain.exception.DomainError;
import com.gabriel.core.domain.exception.DomainException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.stream.Collectors;

public interface DomainValid {

    default void validate() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .sorted()
                .collect(Collectors.joining(", "));
            throw new DomainException("Domain validation failed: " + errorMsg, DomainError.DOM_OO1);
        }
    }
}
