package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.MenuMock;
import com.gabriel.menu.core.application.query.SearchProductQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductMenuUseCaseTest {

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private ProductMenuUseCase productMenuUseCase;

    @BeforeEach
    void setUp() {
        // Assuming there are three categories for simplicity
        Product product1 = MenuMock.generateProduct();
        Product product2 = MenuMock.generateProduct();
        Product product3 = MenuMock.generateProduct();
        Product product4 = MenuMock.generateProduct();

        when(productUseCase.searchProduct(new SearchProductQuery(Category.BURGER)))
            .thenReturn(List.of(product1));
        when(productUseCase.searchProduct(new SearchProductQuery(Category.ACCOMPANIMENT)))
            .thenReturn(List.of(product2));
        when(productUseCase.searchProduct(new SearchProductQuery(Category.DESSERT)))
            .thenReturn(List.of(product3));
        when(productUseCase.searchProduct(new SearchProductQuery(Category.DRINK)))
            .thenReturn(List.of(product4));
    }

    @Test
    void retrieveMenuShouldAggregateProductsFromAllCategories() {
        List<Product> products = productMenuUseCase.retrieveMenu();

        // Assuming Category.values().length equals the number of categories you set up in setUp method
        int expectedNumberOfProducts = Category.values().length;
        assertEquals(expectedNumberOfProducts, products.size(), "The retrieved products list should contain one product per category.");

        // Additionally, you could verify that the productUseCase.searchProduct was called for each category
        for (Category category : Category.values()) {
            verify(productUseCase).searchProduct(new SearchProductQuery(category));
        }
    }
}
