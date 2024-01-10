package com.gabriel.orders.core.application.events;

import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;

public record MenuProductDeletedEvent(ProductID productDeleted) {
}
