package com.gabriel.permissions.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

@DisplayName("Authority Domain Model Tests")
class AuthorityTest {

    @Test
    @DisplayName("Should create authority with all fields")
    fun shouldCreateAuthorityWithAllFields() {
        val id = UUID.randomUUID()
        val name = "READ_USERS"
        val description = "Permission to read users"
        val now = LocalDateTime.now()
        val roleAuthorities = mutableSetOf<RoleAuthority>()

        val authority = Authority(id, name, description, now, now, roleAuthorities)

        assertEquals(id, authority.id)
        assertEquals(name, authority.name)
        assertEquals(description, authority.description)
        assertEquals(now, authority.createdAt)
        assertEquals(now, authority.updatedAt)
        assertEquals(roleAuthorities, authority.roleAuthorities)
    }

    @Test
    @DisplayName("Should create authority with no-arg constructor")
    fun shouldCreateAuthorityWithNoArgConstructor() {
        val authority = Authority()

        assertNotNull(authority)
        assertNull(authority.id)
        assertNull(authority.name)
    }

    @Test
    @DisplayName("Should set and get authority properties")
    fun shouldSetAndGetAuthorityProperties() {
        val authority = Authority()
        val id = UUID.randomUUID()
        val name = "WRITE_USERS"
        val description = "Permission to write users"
        val now = LocalDateTime.now()
        val roleAuthorities = mutableSetOf<RoleAuthority>()

        authority.id = id
        authority.name = name
        authority.description = description
        authority.createdAt = now
        authority.updatedAt = now
        authority.roleAuthorities = roleAuthorities

        assertEquals(id, authority.id)
        assertEquals(name, authority.name)
        assertEquals(description, authority.description)
        assertEquals(now, authority.createdAt)
        assertEquals(now, authority.updatedAt)
        assertEquals(roleAuthorities, authority.roleAuthorities)
    }

    @Test
    @DisplayName("Should generate toString correctly")
    fun shouldGenerateToStringCorrectly() {
        val id = UUID.randomUUID()
        val name = "READ_USERS"
        val description = "Permission to read users"
        val now = LocalDateTime.now()

        val authority = Authority(id, name, description, now, now, mutableSetOf())
        val toString = authority.toString()

        assertTrue(toString.contains("Authority("))
        assertTrue(toString.contains("id=$id"))
        assertTrue(toString.contains("name=READ_USERS"))
        assertTrue(toString.contains("description=Permission to read users"))
    }
}
