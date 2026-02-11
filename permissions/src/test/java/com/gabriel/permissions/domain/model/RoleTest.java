package com.gabriel.permissions.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Role Domain Model Tests")
class RoleTest {

    @Test
    @DisplayName("Should create role with all fields")
    void shouldCreateRoleWithAllFields() {
        UUID id = UUID.randomUUID();
        String name = "ADMIN";
        String description = "Administrator role";
        LocalDateTime now = LocalDateTime.now();
        Set<RoleAuthority> authorities = new HashSet<>();

        Role role = new Role(id, name, description, now, now, authorities);

        assertEquals(id, role.getId());
        assertEquals(name, role.getName());
        assertEquals(description, role.getDescription());
        assertEquals(now, role.getCreatedAt());
        assertEquals(now, role.getUpdatedAt());
        assertEquals(authorities, role.getRoleAuthorities());
    }

    @Test
    @DisplayName("Should create role with no-arg constructor")
    void shouldCreateRoleWithNoArgConstructor() {
        Role role = new Role();

        assertNotNull(role);
        assertNull(role.getId());
        assertNull(role.getName());
    }

    @Test
    @DisplayName("Should set and get role properties")
    void shouldSetAndGetRoleProperties() {
        Role role = new Role();
        UUID id = UUID.randomUUID();
        String name = "USER";
        String description = "Regular user role";
        LocalDateTime now = LocalDateTime.now();
        Set<RoleAuthority> authorities = new HashSet<>();

        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        role.setCreatedAt(now);
        role.setUpdatedAt(now);
        role.setRoleAuthorities(authorities);

        assertEquals(id, role.getId());
        assertEquals(name, role.getName());
        assertEquals(description, role.getDescription());
        assertEquals(now, role.getCreatedAt());
        assertEquals(now, role.getUpdatedAt());
        assertEquals(authorities, role.getRoleAuthorities());
    }

    @Test
    @DisplayName("Should generate toString correctly")
    void shouldGenerateToStringCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "ADMIN";
        String description = "Administrator role";
        LocalDateTime now = LocalDateTime.now();

        Role role = new Role(id, name, description, now, now, new HashSet<>());
        String toString = role.toString();

        assertTrue(toString.contains("Role{"));
        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("name='ADMIN'"));
        assertTrue(toString.contains("description='Administrator role'"));
    }
}
