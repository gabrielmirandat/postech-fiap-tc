package com.gabriel.common.core.domain.model.id;

import com.gabriel.common.core.domain.base.EntityType;
import com.gabriel.common.core.domain.base.ValueObject;

import java.time.LocalDate;
import java.util.UUID;

public abstract class EntityID extends ValueObject {

    protected String generate(EntityType entityType) {
        String timestamp = UUID.randomUUID().toString().split("-")[0];
        LocalDate today = LocalDate.now();
        return String.format("%s-%s-%s", timestamp, entityType.getDefaultRepr(), today);
    }
}
