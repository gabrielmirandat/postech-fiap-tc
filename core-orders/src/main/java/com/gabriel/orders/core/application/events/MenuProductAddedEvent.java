package com.gabriel.orders.core.application.events;

import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;

public record MenuProductAddedEvent(ProductID productAdded) {
}
