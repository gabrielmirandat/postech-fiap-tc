package com.gabriel.menu.core.domain.model;

import com.gabriel.model.Name;
import com.gabriel.model.Price;
import java.time.Instant;

public abstract class Menu {

    protected Instant creationTimestamp;
    protected Instant updateTimestamp;

    public abstract String getMenuId();

    public abstract Name getName();

    public abstract Price getPrice();

    public abstract Category getCategory();

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }
}
