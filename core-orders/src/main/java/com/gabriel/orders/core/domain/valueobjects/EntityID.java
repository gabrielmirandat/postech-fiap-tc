package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.entities.enums.EntityType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

abstract class EntityID extends ValueObject {

    protected String generate(EntityType entityType) {
        String timestamp = UUID.randomUUID().toString().split("-")[0];
        LocalDate today = LocalDate.now();
        return String.format("%s-%s-%s", timestamp, entityType.getDefaultRepr(), today);
    }
}
