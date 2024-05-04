package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.core.domain.model.RoleAuthority;
import com.gabriel.orders.core.domain.port.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionRedisRepository implements PermissionRepository {


    @Override
    public List<RoleAuthority> allRolesAuthorities() {
        return null;
    }

    @Override
    public void addRoleAuthority(RoleAuthority roleAuthority) {

    }

    @Override
    public void updateRoleAuthority(RoleAuthority roleAuthority) {

    }

    @Override
    public void deleteRoleAuthority(String roleName) {

    }
}
