package com.gabriel.permissions.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

@DisplayName("Role Domain Model Tests")
class RoleTest {

    @Test
    @DisplayName("Should create role with all fields")
    fun shouldCreateRoleWithAllFields() {
        val id = UUID.randomUUID()
        val name = "ADMIN"
        val description = "Administrator role"
        val now = LocalDateTime.now()
        val authorities = mutableSetOf<RoleAuthority>()

        val role = Role(id, name, description, now, now, authorities)

        assertEquals(id, role.id)
        assertEquals(name, role.name)
        assertEquals(description, role.description)
        assertEquals(now, role.createdAt)
        assertEquals(now, role.updatedAt)
        assertEquals(authorities, role.roleAuthorities)
    }

    @Test
    @DisplayName("Should create role with no-arg constructor")
    fun shouldCreateRoleWithNoArgConstructor() {
        val role = Role()

        assertNotNull(role)
        assertNull(role.id)
        assertNull(role.name)
    }

    @Test
    @DisplayName("Should set and get role properties")
    fun shouldSetAndGetRoleProperties() {
        val role = Role()
        val id = UUID.randomUUID()
        val name = "USER"
        val description = "Regular user role"
        val now = LocalDateTime.now()
        val authorities = mutableSetOf<RoleAuthority>()

        role.id = id
        role.name = name
        role.description = description
        role.createdAt = now
        role.updatedAt = now
        role.roleAuthorities = authorities

        assertEquals(id, role.id)
        assertEquals(name, role.name)
        assertEquals(description, role.description)
        assertEquals(now, role.createdAt)
        assertEquals(now, role.updatedAt)
        assertEquals(authorities, role.roleAuthorities)
    }

    @Test
    @DisplayName("Should generate toString correctly")
    fun shouldGenerateToStringCorrectly() {
        val id = UUID.randomUUID()
        val name = "ADMIN"
        val description = "Administrator role"
        val now = LocalDateTime.now()

        val role = Role(id, name, description, now, now, mutableSetOf())
        val toString = role.toString()

        assertTrue(toString.contains("Role("))
        assertTrue(toString.contains("id=$id"))
        assertTrue(toString.contains("name=ADMIN"))
        assertTrue(toString.contains("description=Administrator role"))
    }
}
