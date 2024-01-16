package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuCheckUseCase {

    private final MenuRepository menuRepository;

    public MenuCheckUseCase(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void verifyMenu(Order order) {
        order.getItems().forEach(item -> {
            if (!menuRepository.existsProduct(item.getProduct().getProductID())) {
                throw new RuntimeException("Product not found");
            }

            item.getExtras().forEach(extra -> {
                if (!menuRepository.existsExtra(extra.getIngredientID())) {
                    throw new RuntimeException("Extra not found");
                }
            });
        });
    }
}
