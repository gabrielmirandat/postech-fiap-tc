package com.gabriel.orders.adapter.driven.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.model.Permission;
import com.gabriel.core.domain.model.id.PermissionID;
import com.gabriel.orders.core.domain.port.PermissionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PermissionRedisRepository implements PermissionRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, byte[]> redisTemplate;

    public PermissionRedisRepository(ObjectMapper objectMapper,
                                     RedisTemplate<String, byte[]> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    private void showCurrentKeys() {
        System.out.println("Current keys:");
        Objects.requireNonNull(redisTemplate.keys("perm:*"))
            .forEach(System.out::println);
    }

    @Override
    public List<Permission> allPermissions() {
        Set<String> keys = redisTemplate.keys("perm:*");
        List<Permission> permissions = new ArrayList<>();
        if (keys != null) {
            ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
            keys.forEach(key -> {
                try {
                    Permission permission = objectMapper.readValue(valueOps.get(key), Permission.class);
                    permissions.add(permission);
                } catch (IOException e) {
                    throw new IllegalStateException("Error deserializing permission", e);
                }
            });
        }
        return permissions;
    }

    @Override
    public Permission getPermission(PermissionID permissionID) {
        ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
        byte[] data = valueOps.get("perm:" + permissionID.getId());
        if (data != null) {
            try {
                return objectMapper.readValue(data, Permission.class);
            } catch (IOException e) {
                throw new IllegalStateException("Error deserializing permission", e);
            }
        }
        return null;
    }

    @Override
    public void addPermission(Permission permission) {
        String key = "perm:" + permission.getPermissionID().getId();
        Permission existingPermission = getPermission(permission.getPermissionID());
        if (existingPermission == null || permission.getTimestamp().isAfter(existingPermission.getTimestamp())) {
            ValueOperations<String, byte[]> valueOps = redisTemplate.opsForValue();
            try {
                byte[] serializedExtra = objectMapper.writeValueAsBytes(permission);
                valueOps.set(key, serializedExtra);
            } catch (IOException e) {
                throw new IllegalStateException("Error serializing permission", e);
            }
        }
        showCurrentKeys();
    }

    @Override
    public void deletePermission(PermissionID permissionID) {
        redisTemplate.delete("perm:" + permissionID.getId());
    }
}
