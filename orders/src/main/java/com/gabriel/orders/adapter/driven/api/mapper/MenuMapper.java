package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.Id;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.service.menu.MenuItem;
import com.gabriel.service.menu.MenuResponse;

import java.time.Instant;
import java.util.List;

public class MenuMapper {

    public static Product toProduct(MenuItem menuItem) {
        return new Product(
            new ProductId(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos()));
    }

    public static Extra toExtra(MenuItem menuItem) {
        return new Extra(
            new IngredientId(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos())
        );
    }

    public static List<Product> extractProducts(MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.PRODUCT)
            .map(MenuMapper::toProduct)
            .toList();
    }

    public static List<Extra> extractExtras(com.gabriel.service.menu.MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.INGREDIENT)
            .map(MenuMapper::toExtra)
            .toList();
    }
}
