package com.gabriel.orders.core.domain.base;

import java.util.UUID;

public interface DomainEvent {

    default String id() {
        return UUID.randomUUID().toString();
    }

    default String audience() {
        return "external-bounded-context";
    }

    default String context() {
        return "domain";
    }

    public String source();

    public String subject();

    public String type();
}
