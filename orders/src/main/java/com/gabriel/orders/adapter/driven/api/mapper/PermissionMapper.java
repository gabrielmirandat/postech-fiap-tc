package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.core.domain.model.Permission;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static List<Permission> toPermissionList(com.gabriel.specs.permissions.PermissionResponse response) {
        return response.getItemsList().stream()
            .map(item -> new Permission(
                item.getRole(),
                item.getAuthority(),
                Instant.from(Instant.ofEpochSecond(
                    item.getLastUpdated().getSeconds(),
                    item.getLastUpdated().getNanos()))
            ))
            .collect(Collectors.toList());
    }
}
