package com.gabriel.products.core.application.exceptions;

public class IngredientDoNotExistException extends RuntimeException {

    public IngredientDoNotExistException(String message) {
        super(message);
    }
}
