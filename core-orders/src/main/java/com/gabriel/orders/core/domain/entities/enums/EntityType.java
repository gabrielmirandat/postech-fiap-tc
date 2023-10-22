package com.gabriel.orders.core.domain.entities.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EntityType {
    CUSTOMER("cust"),
    ORDER("ordr"),
    PRODUCT("prdc"),
    INGREDIENT("ingr");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }
    public String getDefaultRepr() {
        return this.value;
    }

    public static List<String> getAllDefaultReprs() {
        return Stream.of(EntityType.values())
                .map(EntityType::getDefaultRepr)
                .collect(Collectors.toList());
    }
}
