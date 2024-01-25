package com.gabriel.core.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EntityType {
    CUSTOMER("CUST"),
    ORDER("ORDR"),
    ORDER_ITEM("ORDI"),
    PRODUCT("PRDC"),
    INGREDIENT("INGR");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }

    public static List<String> getAllDefaultReprs() {
        return Stream.of(EntityType.values())
            .map(EntityType::getDefaultRepr)
            .collect(Collectors.toList());
    }

    public String getDefaultRepr() {
        return this.value;
    }
}
