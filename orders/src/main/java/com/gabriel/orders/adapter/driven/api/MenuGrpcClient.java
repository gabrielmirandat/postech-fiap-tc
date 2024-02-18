package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.core.application.usecase.SetupMenuUseCase;
import com.gabriel.specs.menu.MenuGrpc;
import com.gabriel.specs.menu.MenuRequest;
import com.gabriel.specs.menu.MenuResponse;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class MenuGrpcClient {

    private final SetupMenuUseCase setupMenuUseCase;

    private final ManagedChannel managedMenuChannel;

    public MenuGrpcClient(ManagedChannel managedMenuChannel, SetupMenuUseCase setupMenuUseCase) {
        this.managedMenuChannel = managedMenuChannel;
        this.setupMenuUseCase = setupMenuUseCase;
    }

    public void dumpMenuData() {
        System.out.println("Starting grpc client");

        MenuGrpc.MenuBlockingStub stub = MenuGrpc.newBlockingStub(managedMenuChannel);
        MenuRequest request = MenuRequest.newBuilder().setCategory("all").build();
        MenuResponse response = stub.retrieveMenu(request);
        setupMenuUseCase.setupData(response);
        // managedMenuChannel.shutdownNow();
    }
}
