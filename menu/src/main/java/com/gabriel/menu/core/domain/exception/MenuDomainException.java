package com.gabriel.menu.core.domain.exception;

import com.gabriel.core.domain.exception.DomainException;

public class MenuDomainException extends DomainException {

    private final MenuDomainError type;

    public MenuDomainException(String message, MenuDomainError type) {
        super(message, null);
        this.type = type;
    }

    @Override
    public String getType() {
        return type.message;
    }
}
