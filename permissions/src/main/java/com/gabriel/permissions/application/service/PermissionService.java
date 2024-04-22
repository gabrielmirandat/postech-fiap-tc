package com.gabriel.permissions.application.service;

import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException;
import com.gabriel.permissions.domain.repository.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: add hierarchical roles
@Service
public class PermissionService {

    private final RoleRepository roleRepository;

    public PermissionService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> retrieveAllRoles() {
        return roleRepository.findAll();
    }

    public Role retrieveRole(String role) {
        return roleRepository.findByName(role)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role));
    }

    public Set<RoleAuthority> retrieveRoleAuthorities(String role) {

        return roleRepository.findByName(role)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role))
            .getRoleAuthorities();
    }

    public Set<RoleAuthority> retrieveRolesAuthorities(List<String> roles) {
        return roles.stream()
            .map(this::retrieveRoleAuthorities)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRoleGrantedAuthorities(String role) {
        return retrieveRoleAuthorities(role).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRolesGrantedAuthorities(List<String> roles) {
        return retrieveRolesAuthorities(roles).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }
}
