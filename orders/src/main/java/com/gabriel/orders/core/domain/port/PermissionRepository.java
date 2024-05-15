package com.gabriel.orders.core.domain.port;

import com.gabriel.core.domain.model.Permission;
import com.gabriel.core.domain.model.id.PermissionID;

import java.util.List;

public interface PermissionRepository {

    List<Permission> allPermissions();

    Permission getPermission(PermissionID permissionID);

    void addPermission(Permission permission);

    void deletePermission(PermissionID permissionID);
}
