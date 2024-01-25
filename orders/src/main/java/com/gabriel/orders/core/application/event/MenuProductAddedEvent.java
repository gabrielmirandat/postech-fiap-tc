package com.gabriel.orders.core.application.event;

import com.gabriel.orders.core.domain.model.Product;

public record MenuProductAddedEvent(Product productAdded) {
}
