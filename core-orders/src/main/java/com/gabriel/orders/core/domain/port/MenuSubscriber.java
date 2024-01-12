package com.gabriel.orders.core.domain.port;

import com.gabriel.orders.core.application.event.MenuExtraAddedEvent;
import com.gabriel.orders.core.application.event.MenuExtraDeletedEvent;
import com.gabriel.orders.core.application.event.MenuProductAddedEvent;
import com.gabriel.orders.core.application.event.MenuProductDeletedEvent;

public interface MenuSubscriber {

    void addProduct(MenuProductAddedEvent event);

    void deleteProduct(MenuProductDeletedEvent event);

    void addExtra(MenuExtraAddedEvent event);

    void deleteExtra(MenuExtraDeletedEvent event);
}
