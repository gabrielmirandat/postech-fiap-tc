package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.exception.OrderApplicationError;
import com.gabriel.orders.core.application.exception.OrderApplicationException;
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
                throw new OrderApplicationException("Product not found", OrderApplicationError.ORD_100);
            }

            if (item.getExtrasIds() == null) {
                return;
            }
            
            item.getExtrasIds().forEach(extraId -> {
                if (!menuRepository.existsExtra(extraId)) {
                    throw new OrderApplicationException("Extra not found", OrderApplicationError.ORD_101);
                }
            });
        });
    }
}
