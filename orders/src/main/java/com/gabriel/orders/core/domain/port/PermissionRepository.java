package com.gabriel.orders.core.domain.port;

import com.gabriel.model.Permission;
import com.gabriel.model.PermissionId;

import java.util.List;

public interface PermissionRepository {

    List<Permission> allPermissions();

    Permission getPermission(PermissionId permissionId);

    void addPermission(Permission permission);

    void deletePermission(PermissionId permissionId);
}
