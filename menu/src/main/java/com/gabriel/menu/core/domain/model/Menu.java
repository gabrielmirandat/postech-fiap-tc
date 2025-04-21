package com.gabriel.menu.core.domain.model;

import com.gabriel.core.domain.AggregateRoot;
import com.gabriel.model.Name;
import com.gabriel.model.Price;

public abstract class Menu extends AggregateRoot {

    public abstract String getMenuId();

    public abstract Name getName();

    public abstract Price getPrice();

    public abstract Category getCategory();
}
