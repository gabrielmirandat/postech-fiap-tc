package com.gabriel.products.core.application.exception;

public class IngredientDoNotExistException extends RuntimeException {

    public IngredientDoNotExistException(String message) {
        super(message);
    }
}
