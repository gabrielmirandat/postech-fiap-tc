package com.gabriel.orders.core.application.usecase;

import com.gabriel.core.domain.model.RoleAuthority;
import com.gabriel.orders.adapter.driven.api.mapper.PermissionMapper;
import com.gabriel.orders.core.domain.port.PermissionRepository;
import com.gabriel.specs.permissions.RoleAuthoritiesResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetupPermissionUseCase {

    private final PermissionRepository permissionRepository;

    public SetupPermissionUseCase(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void setupData(RoleAuthoritiesResponse inputDumpPermissionsData) {
        System.out.println("Dumping permissions data");

        List<RoleAuthority> rolesAuthorities = PermissionMapper.toRoleAuthorities(inputDumpPermissionsData);

        rolesAuthorities.forEach(permissionRepository::addRoleAuthority);
    }
}
