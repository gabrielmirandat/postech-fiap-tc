package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.application.events.MenuIngredientAddedEvent;
import com.gabriel.orders.core.application.events.MenuIngredientDeletedEvent;
import com.gabriel.orders.core.application.events.MenuProductAddedEvent;
import com.gabriel.orders.core.application.events.MenuProductDeletedEvent;

public interface MenuSubscriber {

    void addProduct(MenuProductAddedEvent event);

    void deleteProduct(MenuProductDeletedEvent event);

    void addIngredient(MenuIngredientAddedEvent event);

    void deleteIngredient(MenuIngredientDeletedEvent event);
}
