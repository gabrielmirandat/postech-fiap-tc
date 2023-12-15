package com.gabriel.products.core.domain.valueobjects.ids;

import com.gabriel.products.core.domain.base.ValueObject;
import com.gabriel.products.core.domain.entities.enums.EntityType;

import java.time.LocalDate;
import java.util.UUID;

abstract class EntityID extends ValueObject {

    protected String generate(EntityType entityType) {
        String timestamp = UUID.randomUUID().toString().split("-")[0];
        LocalDate today = LocalDate.now();
        return String.format("%s-%s-%s", timestamp, entityType.getDefaultRepr(), today);
    }
}
