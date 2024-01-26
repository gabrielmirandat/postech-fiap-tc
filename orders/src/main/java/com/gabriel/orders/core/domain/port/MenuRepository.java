package com.gabriel.orders.core.domain.port;

import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;

public interface MenuRepository {

    boolean existsProduct(ProductID productID);

    Product getProduct(ProductID productID);

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(ProductID productID);

    boolean existsExtra(IngredientID ingredientID);

    Extra getExtra(IngredientID ingredientID);

    void addExtra(Extra extra);

    void updateExtra(Extra extra);

    void deleteExtra(IngredientID ingredientID);
}
