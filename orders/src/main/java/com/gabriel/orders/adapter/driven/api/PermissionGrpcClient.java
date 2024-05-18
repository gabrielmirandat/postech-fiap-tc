package com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.core.application.usecase.SetupPermissionUseCase;
import com.gabriel.specs.permissions.PermissionGrpc;
import com.gabriel.specs.permissions.PermissionRequest;
import com.gabriel.specs.permissions.PermissionResponse;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PermissionGrpcClient {

    private final SetupPermissionUseCase setupPermissionUseCase;

    private final ManagedChannel permissionsManagedChannel;

    public PermissionGrpcClient(@Qualifier("permissionManagedChannel") ManagedChannel permissionsManagedChannel,
                                SetupPermissionUseCase setupPermissionUseCase) {
        this.permissionsManagedChannel = permissionsManagedChannel;
        this.setupPermissionUseCase = setupPermissionUseCase;
    }

    public void dumpPermissionData() {
        System.out.println("Starting grpc permission client");

        try {
            PermissionGrpc.PermissionBlockingStub stub = PermissionGrpc.newBlockingStub(permissionsManagedChannel);
            PermissionRequest request = PermissionRequest.newBuilder().setRole("all").build();
            PermissionResponse response = stub.retrievePermissions(request);
            setupPermissionUseCase.setupData(response);
            // permissionsManagedChannel.shutdownNow();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
