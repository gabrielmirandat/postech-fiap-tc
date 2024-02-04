package com.gabriel.menu.core.application.command;

import com.gabriel.core.domain.model.id.ProductID;

public record DeleteProductCommand(ProductID deleteId) {
}
