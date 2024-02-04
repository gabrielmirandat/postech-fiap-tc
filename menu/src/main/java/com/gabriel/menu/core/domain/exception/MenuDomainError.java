package com.gabriel.menu.core.domain.exception;

public enum MenuDomainError {

    MEN_001("MEN_001 - INVALID INGREDIENT"),
    MEN_002("MEN_002 - ANOTHER");


    final String message;

    MenuDomainError(String message) {
        this.message = message;
    }
}
