package com.gabriel.menu.core.domain.exception;

public class MenuDomainException extends RuntimeException {

    private final MenuDomainError type;

    public MenuDomainException(String message, MenuDomainError type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type.message;
    }
}
