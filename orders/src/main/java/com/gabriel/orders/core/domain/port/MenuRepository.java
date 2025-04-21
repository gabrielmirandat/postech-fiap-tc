package com.gabriel.orders.core.domain.port;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;

import java.util.List;

public interface MenuRepository {


    boolean existsProduct(ProductId productId);

    List<ProductId> allProducts();

    Product getProduct(ProductId productId);

    void addProduct(Product product);

    void deleteProduct(ProductId productId);


    boolean existsExtra(IngredientId ingredientId);

    List<IngredientId> allExtras();

    Extra getExtra(IngredientId ingredientId);

    void addExtra(Extra extra);

    void deleteExtra(IngredientId ingredientId);
}
