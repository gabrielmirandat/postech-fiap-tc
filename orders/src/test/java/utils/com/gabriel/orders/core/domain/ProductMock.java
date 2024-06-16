package utils.com.gabriel.orders.core.domain;

import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.Product;

import java.time.Instant;

import static utils.com.gabriel.orders.core.UtilsMock.generateRandomString;

public class ProductMock {

    public static Product validProduct(ProductID productID) {
        return new Product(productID, new Name(generateRandomString()),
            new Price(10.0), Instant.now());
    }
}
