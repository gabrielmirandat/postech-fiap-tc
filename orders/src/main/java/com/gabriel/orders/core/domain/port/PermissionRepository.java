package com.gabriel.orders.core.domain.port;

import com.gabriel.core.domain.model.Permission;

import java.util.List;

public interface PermissionRepository {

    List<Permission> allPermissions();

    void addPermission(Permission permission);

    void updatePermissionsByRole(String currentRoleName, String newRoleName);

    void updatePermissionsByAuthority(String currentAuthorityName, String newAuthorityName);

    void deletePermission(String roleName, String authorityName);

    void deletePermissionsByRole(String currentRoleName, String newRoleName);
}
