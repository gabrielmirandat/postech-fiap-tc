package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.application.events.MenuExtraAddedEvent;
import com.gabriel.orders.core.application.events.MenuExtraDeletedEvent;
import com.gabriel.orders.core.application.events.MenuProductAddedEvent;
import com.gabriel.orders.core.application.events.MenuProductDeletedEvent;

public interface MenuSubscriber {

    void addProduct(MenuProductAddedEvent event);

    void deleteProduct(MenuProductDeletedEvent event);

    void addExtra(MenuExtraAddedEvent event);

    void deleteExtra(MenuExtraDeletedEvent event);
}
