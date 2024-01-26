package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.specs.menu.MenuGrpc;
import com.gabriel.specs.menu.MenuRequest;
import com.gabriel.specs.menu.MenuResponse;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class MenuGrpcClient {

    private final MenuRepository menuRepository;
    private ManagedChannel managedMenuChannel;

    public MenuGrpcClient(ManagedChannel managedMenuChannel, MenuRepository menuRepository) {
        this.managedMenuChannel = managedMenuChannel;
        this.menuRepository = menuRepository;
    }

    public void bootMenuData() {
        MenuGrpc.MenuBlockingStub stub = MenuGrpc.newBlockingStub(managedMenuChannel);
        MenuRequest request = MenuRequest.newBuilder().build();
        MenuResponse response = stub.retrieveMenu(request);
        response.getItemsList().forEach(item -> {
            System.out.println("Item: " + item.getName() + ", Preço: " + item.getPrice());
        });
        managedMenuChannel.shutdownNow();
    }
}
