package com.gabriel.permissions.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.PermissionId;
import com.gabriel.permissions.domain.model.Authority;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.permissions.domain.model.RoleAuthorityKey;
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException;
import com.gabriel.permissions.domain.repository.AuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleAuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleRepository;
import com.gabriel.permissions.infraestructure.provider.Auth0Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermissionService Unit Tests")
class PermissionServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private RoleAuthorityRepository roleAuthorityRepository;

    @Mock
    private Auth0Provider auth0Provider;

    private PermissionService permissionService;

    private Role testRole;
    private Authority testAuthority;
    private RoleAuthority testRoleAuthority;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        permissionService = new PermissionService(
            roleRepository,
            authorityRepository,
            roleAuthorityRepository,
            "test-issuer.auth0.com",
            auth0Provider,
            objectMapper
        );

        UUID roleId = UUID.randomUUID();
        UUID authorityId = UUID.randomUUID();

        testRole = new Role(
            roleId,
            "ADMIN",
            "Administrator role",
            LocalDateTime.now(),
            LocalDateTime.now(),
            new HashSet<>()
        );

        testAuthority = new Authority(
            authorityId,
            "READ_USERS",
            "Permission to read users",
            LocalDateTime.now(),
            LocalDateTime.now(),
            new HashSet<>()
        );

        RoleAuthorityKey key = new RoleAuthorityKey(roleId, authorityId);
        PermissionId permissionId = PermissionId.newBuilder().setValue("test-perm-id").build();
        testRoleAuthority = new RoleAuthority(key, permissionId, testRole, testAuthority, "admin");
    }

    @Test
    @DisplayName("Should retrieve all roles")
    void shouldRetrieveAllRoles() {
        List<Role> expectedRoles = Arrays.asList(testRole);
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        List<Role> actualRoles = permissionService.retrieveAllRoles();

        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve role by ID")
    void shouldRetrieveRoleById() {
        when(roleRepository.findById(testRole.getId())).thenReturn(Optional.of(testRole));

        Role actualRole = permissionService.retrieveRoleById(testRole.getId());

        assertEquals(testRole, actualRole);
        verify(roleRepository, times(1)).findById(testRole.getId());
    }

    @Test
    @DisplayName("Should throw exception when role not found by ID")
    void shouldThrowExceptionWhenRoleNotFoundById() {
        UUID nonExistentId = UUID.randomUUID();
        when(roleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            permissionService.retrieveRoleById(nonExistentId);
        });

        verify(roleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should retrieve role by name")
    void shouldRetrieveRoleByName() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(testRole));

        Role actualRole = permissionService.retrieveRoleByName("ADMIN");

        assertEquals(testRole, actualRole);
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    @DisplayName("Should throw exception when role not found by name")
    void shouldThrowExceptionWhenRoleNotFoundByName() {
        when(roleRepository.findByName("NON_EXISTENT")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> {
            permissionService.retrieveRoleByName("NON_EXISTENT");
        });

        verify(roleRepository, times(1)).findByName("NON_EXISTENT");
    }

    @Test
    @DisplayName("Should retrieve role authorities by name")
    void shouldRetrieveRoleAuthoritiesByName() {
        Set<RoleAuthority> expectedAuthorities = Set.of(testRoleAuthority);
        testRole.setRoleAuthorities(expectedAuthorities);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(testRole));

        Set<RoleAuthority> actualAuthorities = permissionService.retrieveRoleAuthoritiesByName("ADMIN");

        assertEquals(expectedAuthorities, actualAuthorities);
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    @DisplayName("Should retrieve granted authorities by role name")
    void shouldRetrieveGrantedAuthoritiesByRoleName() {
        Set<RoleAuthority> roleAuthorities = Set.of(testRoleAuthority);
        testRole.setRoleAuthorities(roleAuthorities);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(testRole));

        Set<GrantedAuthority> grantedAuthorities = permissionService.retrieveRoleGrantedAuthoritiesByName("ADMIN");

        assertEquals(1, grantedAuthorities.size());
        assertTrue(grantedAuthorities.stream()
            .anyMatch(ga -> ga.getAuthority().equals("READ_USERS")));
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    @DisplayName("Should list all authorities")
    void shouldListAllAuthorities() {
        List<Authority> expectedAuthorities = Arrays.asList(testAuthority);
        when(authorityRepository.findAll()).thenReturn(expectedAuthorities);

        List<Authority> actualAuthorities = permissionService.listAuthorities();

        assertEquals(expectedAuthorities, actualAuthorities);
        verify(authorityRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create authority")
    void shouldCreateAuthority() {
        when(authorityRepository.save(testAuthority)).thenReturn(testAuthority);

        Authority createdAuthority = permissionService.createAuthority(testAuthority);

        assertEquals(testAuthority, createdAuthority);
        verify(authorityRepository, times(1)).save(testAuthority);
    }

    @Test
    @DisplayName("Should retrieve authority by ID")
    void shouldRetrieveAuthorityById() {
        when(authorityRepository.findById(testAuthority.getId())).thenReturn(Optional.of(testAuthority));

        Authority actualAuthority = permissionService.retrieveAuthorityById(testAuthority.getId());

        assertEquals(testAuthority, actualAuthority);
        verify(authorityRepository, times(1)).findById(testAuthority.getId());
    }

    @Test
    @DisplayName("Should add role authority")
    void shouldAddRoleAuthority() {
        when(roleRepository.findById(testRole.getId())).thenReturn(Optional.of(testRole));
        when(authorityRepository.findById(testAuthority.getId())).thenReturn(Optional.of(testAuthority));
        when(roleAuthorityRepository.save(any(RoleAuthority.class))).thenReturn(testRoleAuthority);

        RoleAuthority createdRoleAuthority = permissionService.addRoleAuthority(
            testRole.getId(),
            testAuthority.getId()
        );

        assertNotNull(createdRoleAuthority);
        verify(roleRepository, times(1)).findById(testRole.getId());
        verify(authorityRepository, times(1)).findById(testAuthority.getId());
        verify(roleAuthorityRepository, times(1)).save(any(RoleAuthority.class));
    }

    @Test
    @DisplayName("Should remove role authority")
    void shouldRemoveRoleAuthority() {
        RoleAuthorityKey key = new RoleAuthorityKey(testRole.getId(), testAuthority.getId());
        doNothing().when(roleAuthorityRepository).deleteById(key);

        permissionService.removeRoleAuthority(testRole.getId(), testAuthority.getId());

        verify(roleAuthorityRepository, times(1)).deleteById(key);
    }

    @Test
    @DisplayName("Should list role authorities")
    void shouldListRoleAuthorities() {
        Set<RoleAuthority> expectedAuthorities = Set.of(testRoleAuthority);
        testRole.setRoleAuthorities(expectedAuthorities);
        when(roleRepository.findById(testRole.getId())).thenReturn(Optional.of(testRole));

        Set<RoleAuthority> actualAuthorities = permissionService.listRoleAuthorities(testRole.getId());

        assertEquals(expectedAuthorities, actualAuthorities);
        verify(roleRepository, times(1)).findById(testRole.getId());
    }

    @Test
    @DisplayName("Should delete authority by ID")
    void shouldDeleteAuthorityById() {
        UUID authorityId = testAuthority.getId();
        doNothing().when(authorityRepository).deleteById(authorityId);

        permissionService.deleteAuthorityById(authorityId);

        verify(authorityRepository, times(1)).deleteById(authorityId);
    }
}
