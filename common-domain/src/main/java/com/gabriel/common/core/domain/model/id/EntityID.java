package com.gabriel.common.core.domain.model.id;

import com.gabriel.common.core.domain.base.EntityType;
import com.gabriel.common.core.domain.base.ValueObject;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public abstract class EntityID extends ValueObject implements Comparable<EntityID> {

    protected abstract String getId();

    @Override
    public int compareTo(EntityID other) {
        return this.getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityID entityID = (EntityID) o;
        return Objects.equals(getId(), entityID.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    protected String generate(EntityType entityType) {
        String timestamp = UUID.randomUUID().toString().split("-")[0];
        LocalDate today = LocalDate.now();
        return String.format("%s-%s-%s", timestamp, entityType.getDefaultRepr(), today);
    }
}
