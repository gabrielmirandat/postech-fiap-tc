package com.gabriel.orders.core.domain.port;

import com.gabriel.core.domain.model.RoleAuthority;

import java.util.List;

public interface PermissionRepository {

    List<RoleAuthority> allRolesAuthorities();

    void addRoleAuthority(RoleAuthority roleAuthority);

    void updateRoleAuthority(RoleAuthority roleAuthority);

    void deleteRoleAuthority(String roleName);
}
