package com.gabriel.core.domain;

import jakarta.persistence.MappedSuperclass;

import java.time.Instant;

@MappedSuperclass
public abstract class AggregateRoot extends Entity {

    protected Instant updateTimestamp;
    protected Instant creationTimestamp;

    public AggregateRoot() {
        this.creationTimestamp = Instant.now();
        this.updateTimestamp = Instant.now();
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }
}
