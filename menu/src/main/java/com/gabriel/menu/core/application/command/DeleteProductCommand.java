package com.gabriel.menu.core.application.command;

import com.gabriel.model.ProductId;

public record DeleteProductCommand(ProductId deleteId) {
}
