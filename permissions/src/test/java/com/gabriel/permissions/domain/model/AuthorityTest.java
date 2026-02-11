package com.gabriel.permissions.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authority Domain Model Tests")
class AuthorityTest {

    @Test
    @DisplayName("Should create authority with all fields")
    void shouldCreateAuthorityWithAllFields() {
        UUID id = UUID.randomUUID();
        String name = "READ_USERS";
        String description = "Permission to read users";
        LocalDateTime now = LocalDateTime.now();
        Set<RoleAuthority> roleAuthorities = new HashSet<>();

        Authority authority = new Authority(id, name, description, now, now, roleAuthorities);

        assertEquals(id, authority.getId());
        assertEquals(name, authority.getName());
        assertEquals(description, authority.getDescription());
        assertEquals(now, authority.getCreatedAt());
        assertEquals(now, authority.getUpdatedAt());
        assertEquals(roleAuthorities, authority.getRoleAuthorities());
    }

    @Test
    @DisplayName("Should create authority with no-arg constructor")
    void shouldCreateAuthorityWithNoArgConstructor() {
        Authority authority = new Authority();

        assertNotNull(authority);
        assertNull(authority.getId());
        assertNull(authority.getName());
    }

    @Test
    @DisplayName("Should set and get authority properties")
    void shouldSetAndGetAuthorityProperties() {
        Authority authority = new Authority();
        UUID id = UUID.randomUUID();
        String name = "WRITE_USERS";
        String description = "Permission to write users";
        LocalDateTime now = LocalDateTime.now();
        Set<RoleAuthority> roleAuthorities = new HashSet<>();

        authority.setId(id);
        authority.setName(name);
        authority.setDescription(description);
        authority.setCreatedAt(now);
        authority.setUpdatedAt(now);
        authority.setRoleAuthorities(roleAuthorities);

        assertEquals(id, authority.getId());
        assertEquals(name, authority.getName());
        assertEquals(description, authority.getDescription());
        assertEquals(now, authority.getCreatedAt());
        assertEquals(now, authority.getUpdatedAt());
        assertEquals(roleAuthorities, authority.getRoleAuthorities());
    }

    @Test
    @DisplayName("Should generate toString correctly")
    void shouldGenerateToStringCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "READ_USERS";
        String description = "Permission to read users";
        LocalDateTime now = LocalDateTime.now();

        Authority authority = new Authority(id, name, description, now, now, new HashSet<>());
        String toString = authority.toString();

        assertTrue(toString.contains("Authority{"));
        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("name='READ_USERS'"));
        assertTrue(toString.contains("description='Permission to read users'"));
    }
}
