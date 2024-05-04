package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.core.application.usecase.SetupMenuUseCase;
import com.gabriel.specs.menu.MenuGrpc;
import com.gabriel.specs.menu.MenuRequest;
import com.gabriel.specs.menu.MenuResponse;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PermissionGrpcClient {

    private final SetupMenuUseCase setupMenuUseCase;

    private final ManagedChannel permissionsManagedChannel;

    public PermissionGrpcClient(@Qualifier("permissionManagedChannel") ManagedChannel permissionsManagedChannel,
                                SetupMenuUseCase setupMenuUseCase) {
        this.permissionsManagedChannel = permissionsManagedChannel;
        this.setupMenuUseCase = setupMenuUseCase;
    }

    public void dumpMenuData() {
        System.out.println("Starting grpc client");

        try {
            MenuGrpc.MenuBlockingStub stub = MenuGrpc.newBlockingStub(permissionsManagedChannel);
            MenuRequest request = MenuRequest.newBuilder().setCategory("all").build();
            MenuResponse response = stub.retrieveMenu(request);
            setupMenuUseCase.setupData(response);
            // managedMenuChannel.shutdownNow();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
