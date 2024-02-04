package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.core.domain.EntityType;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.EntityID;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.specs.menu.MenuItem;
import com.gabriel.specs.menu.MenuResponse;

import java.time.Instant;
import java.util.List;

public class MenuMapper {

    public static Product toProduct(MenuItem menuItem) {
        return new Product(
            new ProductID(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.from(Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos())));
    }

    public static Extra toExtra(MenuItem menuItem) {
        return new Extra(
            new IngredientID(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.from(Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos()))
        );
    }

    public static List<Product> extractProducts(MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.PRODUCT)
            .map(MenuMapper::toProduct)
            .toList();
    }

    public static List<Extra> extractExtras(com.gabriel.specs.menu.MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.INGREDIENT)
            .map(MenuMapper::toExtra)
            .toList();
    }
}
