package com.gabriel.orders.core.domain.port;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;

import java.util.List;

public interface MenuRepository {


    boolean existsProduct(ProductID productID);

    List<ProductID> allProducts();

    Product getProduct(ProductID productID);

    void addProduct(Product product);

    void deleteProduct(ProductID productID);


    boolean existsExtra(IngredientID ingredientID);

    List<IngredientID> allExtras();

    Extra getExtra(IngredientID ingredientID);

    void addExtra(Extra extra);

    void deleteExtra(IngredientID ingredientID);
}
