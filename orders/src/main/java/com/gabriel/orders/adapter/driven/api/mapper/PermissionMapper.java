package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.model.Name;
import com.gabriel.model.Permission;
import com.gabriel.model.PermissionId;

import java.util.List;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static List<Permission> toPermissionList(com.gabriel.service.permissions.PermissionResponse response) {
        return response.getItemsList().stream()
            .map(item -> Permission.newBuilder()
                .setPermissionId(PermissionId.newBuilder().setValue(item.getId()).build())
                .setRoleName(Name.newBuilder().setValue(item.getRole()).build())
                .setAuthorityName(Name.newBuilder().setValue(item.getAuthority()).build())
                .setTimestamp(item.getLastUpdated())
                .build())
            .collect(Collectors.toList());
    }
}
