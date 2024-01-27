package com.gabriel.menu.core.domain.model;

import com.gabriel.domain.AggregateRoot;
import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;

public abstract class Menu extends AggregateRoot {
    public abstract String getMenuId();

    public abstract Name getName();

    public abstract Price getPrice();

    public abstract Category getCategory();
}
