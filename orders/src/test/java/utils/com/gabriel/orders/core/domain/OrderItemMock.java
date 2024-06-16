package utils.com.gabriel.orders.core.domain;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.orders.core.domain.model.Product;

import java.util.Collections;

import static utils.com.gabriel.orders.core.domain.ExtraMock.validExtra;
import static utils.com.gabriel.orders.core.domain.ProductMock.validProduct;

public class OrderItemMock {

    public static OrderItem validOrderItem(boolean withExtra) {
        Product product = validProduct(new ProductID());

        if (withExtra) {
            Extra extra = validExtra(new IngredientID());
            return new OrderItem(product, Collections.singletonList(extra));
        }

        return new OrderItem(product);
    }
}
