package com.gabriel.orders.adapter.driven.api.mapper;

import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Permission;
import com.gabriel.core.domain.model.id.PermissionID;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionMapper {

    public static List<Permission> toPermissionList(com.gabriel.specs.permissions.PermissionResponse response) {
        return response.getItemsList().stream()
            .map(item -> new Permission(
                new PermissionID(item.getId()),
                new Name(item.getRole()),
                new Name(item.getAuthority()),
                Instant.from(Instant.ofEpochSecond(
                    item.getLastUpdated().getSeconds(),
                    item.getLastUpdated().getNanos()))
            ))
            .collect(Collectors.toList());
    }
}
