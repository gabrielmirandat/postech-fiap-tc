package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.MenuDeletedEvent;
import com.gabriel.orders.core.domain.events.MenuUpdatedEvent;

public interface MenuSubscriber {

    void addProduct(MenuAddedEvent event);

    void updateProduct(MenuUpdatedEvent event);

    void deleteProduct(MenuDeletedEvent event);
}
