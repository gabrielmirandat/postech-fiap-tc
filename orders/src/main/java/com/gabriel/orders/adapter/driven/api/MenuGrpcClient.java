package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.core.application.usecase.SetupMenuUseCase;
import com.gabriel.specs.menu.MenuGrpc;
import com.gabriel.specs.menu.MenuRequest;
import com.gabriel.specs.menu.MenuResponse;
import io.grpc.ManagedChannel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class MenuGrpcClient {

    private final SetupMenuUseCase setupMenuUseCase;

    private ManagedChannel managedMenuChannel;

    public MenuGrpcClient(ManagedChannel managedMenuChannel, SetupMenuUseCase setupMenuUseCase) {
        this.managedMenuChannel = managedMenuChannel;
        this.setupMenuUseCase = setupMenuUseCase;
    }

    @PostConstruct
    public void dumpMenuData() {
        System.out.println("Dumping menu data");

        MenuGrpc.MenuBlockingStub stub = MenuGrpc.newBlockingStub(managedMenuChannel);
        MenuRequest request = MenuRequest.newBuilder().build();
        MenuResponse response = stub.retrieveMenu(request);
        setupMenuUseCase.setupData(response);
        managedMenuChannel.shutdownNow();
    }
}
