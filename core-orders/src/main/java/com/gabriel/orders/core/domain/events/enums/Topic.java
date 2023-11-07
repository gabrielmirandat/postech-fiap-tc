package com.gabriel.orders.core.domain.events.enums;

public enum Topic {
    ORDERS("orders"),

    MENU("menub");

    private final String name;

    Topic(String name) {
        this.name = name;
    }

    public String topicName() {
        return this.name;
    }
}
