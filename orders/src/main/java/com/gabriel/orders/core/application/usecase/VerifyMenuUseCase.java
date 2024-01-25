package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class VerifyMenuUseCase {

    private final MenuRepository menuRepository;

    public VerifyMenuUseCase(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validate(CreateOrderCommand command) {
        command.items().forEach(item -> {
            if (!menuRepository.existsProduct(item.getProductId())) {
                throw new RuntimeException("Product not found");
            }

            item.getExtrasIds().forEach(extraId -> {
                if (!menuRepository.existsExtra(extraId)) {
                    throw new RuntimeException("Extra not found");
                }
            });
        });
    }
}
