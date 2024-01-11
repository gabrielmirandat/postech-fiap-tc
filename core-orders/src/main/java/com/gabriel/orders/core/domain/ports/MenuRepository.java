package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.domain.valueobjects.Extra;
import com.gabriel.orders.core.domain.valueobjects.Product;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;

public interface MenuRepository {

    boolean existsProduct(ProductID productID);

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(ProductID productID);

    boolean existsExtra(IngredientID ingredientID);

    void addExtra(Extra extra);

    void updateExtra(Extra extra);

    void deleteExtra(IngredientID ingredientID);
}
