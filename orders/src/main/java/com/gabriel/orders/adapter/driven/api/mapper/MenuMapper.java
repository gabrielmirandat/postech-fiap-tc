package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.domain.EntityType;
import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.EntityID;
import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.specs.menu.MenuItem;
import com.gabriel.specs.menu.MenuResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MenuMapper {

    public Product toProduct(MenuItem menuItem) {
        return new Product(
            new ProductID(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.from(Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos())));
    }

    public Extra toExtra(MenuItem menuItem) {
        return new Extra(
            new IngredientID(menuItem.getId()),
            new Name(menuItem.getName()),
            new Price(menuItem.getPrice()),
            Instant.from(Instant.ofEpochSecond(
                menuItem.getLastUpdated().getSeconds(),
                menuItem.getLastUpdated().getNanos()))
        );
    }

    public List<Product> extractProducts(MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.PRODUCT)
            .map(this::toProduct)
            .toList();
    }

    public List<Extra> extractExtras(com.gabriel.specs.menu.MenuResponse response) {
        return response.getItemsList().stream()
            .filter(item -> EntityID.identify(item.getId()) == EntityType.INGREDIENT)
            .map(this::toExtra)
            .toList();
    }
}
