package com.gabriel.orders.core.domain.port;

import com.gabriel.orders.core.application.event.MenuExtraAddedEvent;
import com.gabriel.orders.core.application.event.MenuExtraDeletedEvent;
import com.gabriel.orders.core.application.event.MenuProductAddedEvent;
import com.gabriel.orders.core.application.event.MenuProductDeletedEvent;

public interface MenuSubscriber {

    void listenProductAdded(MenuProductAddedEvent event);

    void listenProductDeleted(MenuProductDeletedEvent event);

    void listenExtraAdded(MenuExtraAddedEvent event);

    void listenExtraDeleted(MenuExtraDeletedEvent event);
}
