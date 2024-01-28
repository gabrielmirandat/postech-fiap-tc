package com.gabriel.domain.model.id;

import com.gabriel.domain.EntityType;
import com.gabriel.domain.ValueObject;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public abstract class EntityID extends ValueObject implements Comparable<EntityID> {

    public static EntityType identify(String id) {
        String typeIdentifier = id.split("-")[1];
        return Arrays.stream(EntityType.values())
            .filter(e -> e.getCode().equals(typeIdentifier))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown entity type: " + typeIdentifier));
    }

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
        return String.format("%s-%s-%s", timestamp, entityType.getCode(), today);
    }
}
