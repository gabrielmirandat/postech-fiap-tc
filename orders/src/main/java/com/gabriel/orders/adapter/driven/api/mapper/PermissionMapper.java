package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.model.Name;
import com.gabriel.model.Permission;
import com.gabriel.model.PermissionId;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static List<Permission> toPermissionList(com.gabriel.service.permissions.PermissionResponse response) {
        return response.getItemsList().stream()
            .map(item -> new Permission(
                new PermissionID(item.getId()),
                new Name(item.getRole()),
                new Name(item.getAuthority()),
                Instant.ofEpochSecond(
                    item.getLastUpdated().getSeconds(),
                    item.getLastUpdated().getNanos())
            ))
            .collect(Collectors.toList());
    }
}
