package com.gabriel.permissions.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.gabriel.model.PermissionId
import com.gabriel.permissions.domain.model.Authority
import com.gabriel.permissions.domain.model.Role
import com.gabriel.permissions.domain.model.RoleAuthority
import com.gabriel.permissions.domain.model.RoleAuthorityKey
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException
import com.gabriel.permissions.domain.repository.AuthorityRepository
import com.gabriel.permissions.domain.repository.RoleAuthorityRepository
import com.gabriel.permissions.domain.repository.RoleRepository
import com.gabriel.permissions.infraestructure.provider.Auth0Provider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(MockitoExtension::class)
@DisplayName("PermissionService Unit Tests")
class PermissionServiceTest {

    @Mock
    lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var authorityRepository: AuthorityRepository

    @Mock
    lateinit var roleAuthorityRepository: RoleAuthorityRepository

    @Mock
    lateinit var auth0Provider: Auth0Provider

    private lateinit var permissionService: PermissionService
    private lateinit var testRole: Role
    private lateinit var testAuthority: Authority
    private lateinit var testRoleAuthority: RoleAuthority

    @BeforeEach
    fun setUp() {
        val objectMapper = ObjectMapper()
        permissionService = PermissionService(
            roleRepository,
            authorityRepository,
            roleAuthorityRepository,
            "test-issuer.auth0.com",
            auth0Provider,
            objectMapper
        )

        val roleId = UUID.randomUUID()
        val authorityId = UUID.randomUUID()

        testRole = Role(
            roleId,
            "ADMIN",
            "Administrator role",
            LocalDateTime.now(),
            LocalDateTime.now(),
            mutableSetOf()
        )

        testAuthority = Authority(
            authorityId,
            "READ_USERS",
            "Permission to read users",
            LocalDateTime.now(),
            LocalDateTime.now(),
            mutableSetOf()
        )

        val key = RoleAuthorityKey(roleId, authorityId)
        val permissionId = PermissionId.newBuilder().setValue("test-perm-id").build()
        testRoleAuthority = RoleAuthority(key, permissionId, testRole, testAuthority, "admin")
    }

    @Test
    @DisplayName("Should retrieve all roles")
    fun shouldRetrieveAllRoles() {
        val expectedRoles = listOf(testRole)
        `when`(roleRepository.listAll()).thenReturn(expectedRoles)

        val actualRoles = permissionService.retrieveAllRoles()

        assertEquals(expectedRoles, actualRoles)
        verify(roleRepository, times(1)).listAll()
    }

    @Test
    @DisplayName("Should retrieve role by ID")
    fun shouldRetrieveRoleById() {
        `when`(roleRepository.findById(testRole.id!!)).thenReturn(testRole)

        val actualRole = permissionService.retrieveRoleById(testRole.id!!)

        assertEquals(testRole, actualRole)
        verify(roleRepository, times(1)).findById(testRole.id!!)
    }

    @Test
    @DisplayName("Should throw exception when role not found by ID")
    fun shouldThrowExceptionWhenRoleNotFoundById() {
        val nonExistentId = UUID.randomUUID()
        `when`(roleRepository.findById(nonExistentId)).thenReturn(null)

        assertThrows(RoleNotFoundException::class.java) {
            permissionService.retrieveRoleById(nonExistentId)
        }

        verify(roleRepository, times(1)).findById(nonExistentId)
    }

    @Test
    @DisplayName("Should retrieve role by name")
    fun shouldRetrieveRoleByName() {
        `when`(roleRepository.findByName("ADMIN")).thenReturn(testRole)

        val actualRole = permissionService.retrieveRoleByName("ADMIN")

        assertEquals(testRole, actualRole)
        verify(roleRepository, times(1)).findByName("ADMIN")
    }

    @Test
    @DisplayName("Should throw exception when role not found by name")
    fun shouldThrowExceptionWhenRoleNotFoundByName() {
        `when`(roleRepository.findByName("NON_EXISTENT")).thenReturn(null)

        assertThrows(RoleNotFoundException::class.java) {
            permissionService.retrieveRoleByName("NON_EXISTENT")
        }

        verify(roleRepository, times(1)).findByName("NON_EXISTENT")
    }

    @Test
    @DisplayName("Should retrieve role authorities by name")
    fun shouldRetrieveRoleAuthoritiesByName() {
        val expectedAuthorities = setOf(testRoleAuthority)
        testRole.roleAuthorities = expectedAuthorities.toMutableSet()
        `when`(roleRepository.findByName("ADMIN")).thenReturn(testRole)

        val actualAuthorities = permissionService.retrieveRoleAuthoritiesByName("ADMIN")

        assertEquals(expectedAuthorities, actualAuthorities)
        verify(roleRepository, times(1)).findByName("ADMIN")
    }

    @Test
    @DisplayName("Should retrieve authority names by role names")
    fun shouldRetrieveRolesAuthorityNamesByName() {
        val roleAuthorities = setOf(testRoleAuthority)
        testRole.roleAuthorities = roleAuthorities.toMutableSet()
        `when`(roleRepository.findByName("ADMIN")).thenReturn(testRole)

        val authorityNames = permissionService.retrieveRolesAuthorityNamesByName(listOf("ADMIN"))

        assertEquals(1, authorityNames.size)
        assertTrue(authorityNames.contains("READ_USERS"))
        verify(roleRepository, times(1)).findByName("ADMIN")
    }

    @Test
    @DisplayName("Should list all authorities")
    fun shouldListAllAuthorities() {
        val expectedAuthorities = listOf(testAuthority)
        `when`(authorityRepository.listAll()).thenReturn(expectedAuthorities)

        val actualAuthorities = permissionService.listAuthorities()

        assertEquals(expectedAuthorities, actualAuthorities)
        verify(authorityRepository, times(1)).listAll()
    }

    @Test
    @DisplayName("Should create authority")
    fun shouldCreateAuthority() {
        doNothing().`when`(authorityRepository).persist(testAuthority)

        val createdAuthority = permissionService.createAuthority(testAuthority)

        assertEquals(testAuthority, createdAuthority)
        verify(authorityRepository, times(1)).persist(testAuthority)
    }

    @Test
    @DisplayName("Should retrieve authority by ID")
    fun shouldRetrieveAuthorityById() {
        `when`(authorityRepository.findById(testAuthority.id!!)).thenReturn(testAuthority)

        val actualAuthority = permissionService.retrieveAuthorityById(testAuthority.id!!)

        assertEquals(testAuthority, actualAuthority)
        verify(authorityRepository, times(1)).findById(testAuthority.id!!)
    }

    @Test
    @DisplayName("Should add role authority")
    fun shouldAddRoleAuthority() {
        `when`(roleRepository.findById(testRole.id!!)).thenReturn(testRole)
        `when`(authorityRepository.findById(testAuthority.id!!)).thenReturn(testAuthority)
        doNothing().`when`(roleAuthorityRepository).persist(any(RoleAuthority::class.java))

        val createdRoleAuthority = permissionService.addRoleAuthority(
            testRole.id!!,
            testAuthority.id!!
        )

        assertNotNull(createdRoleAuthority)
        verify(roleRepository, times(1)).findById(testRole.id!!)
        verify(authorityRepository, times(1)).findById(testAuthority.id!!)
        verify(roleAuthorityRepository, times(1)).persist(any(RoleAuthority::class.java))
    }

    @Test
    @DisplayName("Should remove role authority")
    fun shouldRemoveRoleAuthority() {
        val key = RoleAuthorityKey(testRole.id, testAuthority.id)
        doNothing().`when`(roleAuthorityRepository).deleteById(key)

        permissionService.removeRoleAuthority(testRole.id!!, testAuthority.id!!)

        verify(roleAuthorityRepository, times(1)).deleteById(key)
    }

    @Test
    @DisplayName("Should list role authorities")
    fun shouldListRoleAuthorities() {
        val expectedAuthorities = setOf(testRoleAuthority)
        testRole.roleAuthorities = expectedAuthorities.toMutableSet()
        `when`(roleRepository.findById(testRole.id!!)).thenReturn(testRole)

        val actualAuthorities = permissionService.listRoleAuthorities(testRole.id!!)

        assertEquals(expectedAuthorities, actualAuthorities)
        verify(roleRepository, times(1)).findById(testRole.id!!)
    }

    @Test
    @DisplayName("Should delete authority by ID")
    fun shouldDeleteAuthorityById() {
        val authorityId = testAuthority.id!!
        doNothing().`when`(authorityRepository).deleteById(authorityId)

        permissionService.deleteAuthorityById(authorityId)

        verify(authorityRepository, times(1)).deleteById(authorityId)
    }
}
