package com.gabriel.domain;

public enum EntityType {
    CUSTOMER("CUST"),
    ORDER("ORDR"),
    ORDER_ITEM("ORDI"),
    PRODUCT("PRDC"),
    INGREDIENT("INGR");

    private final String code;

    EntityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
