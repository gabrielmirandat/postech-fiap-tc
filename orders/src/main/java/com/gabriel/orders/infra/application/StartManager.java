package com.gabriel.orders.infra.application;

import com.gabriel.orders.adapter.driven.api.MenuGrpcClient;
import com.gabriel.orders.adapter.driven.api.PermissionGrpcClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartManager {

    private final MenuGrpcClient menuGrpcClient;
    private final PermissionGrpcClient permissionGrpcClient;

    public StartManager(MenuGrpcClient menuGrpcClient,
                        PermissionGrpcClient permissionGrpcClient) {
        this.menuGrpcClient = menuGrpcClient;
        this.permissionGrpcClient = permissionGrpcClient;
    }

    @PostConstruct
    public void exec() {
        menuGrpcClient.dumpMenuData();
        permissionGrpcClient.dumpPermissionData();
    }
}