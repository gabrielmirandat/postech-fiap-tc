package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.core.domain.model.RoleAuthority;

import java.util.List;

public class PermissionMapper {

    public static List<RoleAuthority> toRoleAuthorities(com.gabriel.specs.permissions.RoleAuthoritiesResponse response) {
        return response.getRoleAuthoritiesList().stream()
            .map(roleAuthority -> new RoleAuthority(
                roleAuthority.getRole(),
                roleAuthority.getAuthoritiesList()))
            .toList();
    }
}
