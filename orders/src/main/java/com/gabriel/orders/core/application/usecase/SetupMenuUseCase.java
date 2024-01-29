package com.gabriel.orders.core.application.usecase;

import com.gabriel.orders.adapter.driven.api.mapper.MenuMapper;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.specs.menu.MenuResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetupMenuUseCase {

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    public SetupMenuUseCase(MenuRepository menuRepository, MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    public void setupData(MenuResponse inputDumpMenuData) {
        System.out.println("Dumping menu data");

        List<Product> products = menuMapper.extractProducts(inputDumpMenuData);
        List<Extra> extras = menuMapper.extractExtras(inputDumpMenuData);

        products.forEach(menuRepository::addProduct);
        extras.forEach(menuRepository::addExtra);
    }
}
