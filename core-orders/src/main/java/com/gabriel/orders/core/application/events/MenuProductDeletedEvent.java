package com.gabriel.orders.core.application.events;

import com.gabriel.orders.core.domain.valueobjects.Product;

public record MenuProductDeletedEvent(Product productDeleted) {
}
