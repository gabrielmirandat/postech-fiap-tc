package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateMenuUseCase {

    private final MenuRepository menuRepository;

    public UpdateMenuUseCase(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void handleProductAdded(Product product) {
        System.out.println("Produto adicionado: " + product);
        menuRepository.addProduct(product);
    }

    public void handleProductDeleted(Product product) {
        System.out.println("Produto deletado: " + product);
        menuRepository.deleteProduct(product.getProductID());
    }

    public void handleExtraAdded(Extra extra) {
        System.out.println("Ingrediente adicionado: " + extra);
        menuRepository.addExtra(extra);
    }

    public void handleExtraDeleted(Extra extra) {
        System.out.println("Ingrediente deletado: " + extra);
        menuRepository.deleteExtra(extra.getIngredientID());
    }
}
