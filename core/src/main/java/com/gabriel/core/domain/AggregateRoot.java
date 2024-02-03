package com.gabriel.core.domain;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class AggregateRoot extends Entity {

    protected Instant updateTimestamp;
    protected Instant creationTimestamp;

    public AggregateRoot() {
        this.creationTimestamp = Instant.now();
        this.updateTimestamp = Instant.now();
    }
}
