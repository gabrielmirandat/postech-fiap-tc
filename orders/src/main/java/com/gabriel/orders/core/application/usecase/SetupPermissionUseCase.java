package com.gabriel.orders.core.application.usecase;

import com.gabriel.core.domain.model.Permission;
import com.gabriel.orders.adapter.driven.api.mapper.PermissionMapper;
import com.gabriel.orders.core.domain.port.PermissionRepository;
import com.gabriel.specs.permissions.PermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetupPermissionUseCase {

    private final PermissionRepository permissionRepository;

    public SetupPermissionUseCase(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void setupData(PermissionResponse inputDumpData) {
        System.out.println("Dumping permissions data");

        List<Permission> permissions = PermissionMapper.toPermissionList(inputDumpData);

        permissions.forEach(permissionRepository::addPermission);
    }
}
