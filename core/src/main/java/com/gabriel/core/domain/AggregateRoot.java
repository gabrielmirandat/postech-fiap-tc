package com.gabriel.core.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class AggregateRoot extends Entity {

    protected Instant updateTimestamp;
    protected Instant creationTimestamp;

    public AggregateRoot() {
        this.creationTimestamp = Instant.now();
        this.updateTimestamp = Instant.now();
    }
}
