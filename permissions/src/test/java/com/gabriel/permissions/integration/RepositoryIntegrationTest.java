package com.gabriel.permissions.integration;

import com.gabriel.permissions.PermissionsApplication;
import com.gabriel.permissions.domain.model.Authority;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.repository.AuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PermissionsApplication.class)
@Testcontainers
@DisplayName("Repository Integration Tests")
class RepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("permissions_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        // Disable Auth0 for integration tests
        registry.add("auth0.enabled", () -> "false");
        registry.add("auth0.issuer", () -> "test-issuer.auth0.com");
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
            () -> "https://test-issuer.auth0.com/");
    }

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    // @Test
    // @DisplayName("Should save and retrieve role")
    // void shouldSaveAndRetrieveRole() {
    //     Role role = new Role();
    //     role.setName("TEST_ROLE");
    //     role.setDescription("Test role description");
    //
    //     Role savedRole = roleRepository.save(role);
    //
    //     assertNotNull(savedRole.getId());
    //     assertNotNull(savedRole.getCreatedAt());
    //     assertNotNull(savedRole.getUpdatedAt());
    //
    //     Optional<Role> foundRole = roleRepository.findByName("TEST_ROLE");
    //     assertTrue(foundRole.isPresent());
    //     assertEquals("TEST_ROLE", foundRole.get().getName());
    //     assertEquals("Test role description", foundRole.get().getDescription());
    // }

    // @Test
    // @DisplayName("Should save and retrieve authority")
    // void shouldSaveAndRetrieveAuthority() {
    //     Authority authority = new Authority();
    //     authority.setName("TEST_AUTHORITY");
    //     authority.setDescription("Test authority description");
    //
    //     Authority savedAuthority = authorityRepository.save(authority);
    //
    //     assertNotNull(savedAuthority.getId());
    //     assertNotNull(savedAuthority.getCreatedAt());
    //     assertNotNull(savedAuthority.getUpdatedAt());
    //
    //     Optional<Authority> foundAuthority = authorityRepository.findById(savedAuthority.getId());
    //     assertTrue(foundAuthority.isPresent());
    //     assertEquals("TEST_AUTHORITY", foundAuthority.get().getName());
    //     assertEquals("Test authority description", foundAuthority.get().getDescription());
    // }

    // @Test
    // @DisplayName("Should enforce unique constraint on role name")
    // void shouldEnforceUniqueConstraintOnRoleName() {
    //     Role role1 = new Role();
    //     role1.setName("UNIQUE_ROLE");
    //     role1.setDescription("First role");
    //     roleRepository.save(role1);
    //
    //     Role role2 = new Role();
    //     role2.setName("UNIQUE_ROLE");
    //     role2.setDescription("Second role");
    //
    //     assertThrows(Exception.class, () -> {
    //         roleRepository.save(role2);
    //         roleRepository.flush();
    //     });
    // }

    // @Test
    // @DisplayName("Should enforce unique constraint on authority name")
    // void shouldEnforceUniqueConstraintOnAuthorityName() {
    //     Authority authority1 = new Authority();
    //     authority1.setName("UNIQUE_AUTHORITY");
    //     authority1.setDescription("First authority");
    //     authorityRepository.save(authority1);
    //
    //     Authority authority2 = new Authority();
    //     authority2.setName("UNIQUE_AUTHORITY");
    //     authority2.setDescription("Second authority");
    //
    //     assertThrows(Exception.class, () -> {
    //         authorityRepository.save(authority2);
    //         authorityRepository.flush();
    //     });
    // }

    // @Test
    // @DisplayName("Should delete role by ID")
    // void shouldDeleteRoleById() {
    //     Role role = new Role();
    //     role.setName("DELETABLE_ROLE");
    //     role.setDescription("Role to be deleted");
    //     Role savedRole = roleRepository.save(role);
    //
    //     roleRepository.deleteById(savedRole.getId());
    //
    //     Optional<Role> deletedRole = roleRepository.findById(savedRole.getId());
    //     assertFalse(deletedRole.isPresent());
    // }

    // @Test
    // @DisplayName("Should update role")
    // void shouldUpdateRole() {
    //     Role role = new Role();
    //     role.setName("UPDATABLE_ROLE");
    //     role.setDescription("Original description");
    //     Role savedRole = roleRepository.save(role);
    //
    //     savedRole.setDescription("Updated description");
    //     Role updatedRole = roleRepository.save(savedRole);
    //
    //     assertEquals("Updated description", updatedRole.getDescription());
    //     assertNotEquals(savedRole.getCreatedAt(), updatedRole.getUpdatedAt());
    // }
}
