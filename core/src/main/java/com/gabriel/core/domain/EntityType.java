package com.gabriel.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    PERMISSION("PERM"),
    CUSTOMER("CUST"),
    ORDER("ORDR"),
    ORDER_ITEM("ORDI"),
    PRODUCT("PRDC"),
    INGREDIENT("INGR");

    private final String code;
}
