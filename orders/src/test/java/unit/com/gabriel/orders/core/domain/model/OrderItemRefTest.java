package unit.com.gabriel.orders.core.domain.model;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.OrderItemRef;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemRefTest {

    @Test
    public void testOrderItemRef() {
        String productId = new ProductID().getId();
        String ingredientID = new IngredientID().getId();
        OrderItemRef orderItemRef = new OrderItemRef(productId, Collections.singletonList(ingredientID));

        assertThat(orderItemRef).isNotNull();
        assertThat(orderItemRef.getProductId().getId()).isEqualTo(productId);
        assertThat(orderItemRef.getExtrasIds().size()).isEqualTo(1);
        assertThat(orderItemRef.getExtrasIds().get(0).getId()).isEqualTo(ingredientID);
    }
}
