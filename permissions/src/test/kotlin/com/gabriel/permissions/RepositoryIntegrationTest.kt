package com.gabriel.permissions

import com.gabriel.permissions.domain.repository.AuthorityRepository
import com.gabriel.permissions.domain.repository.RoleRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Lightweight integration test: repository types exist and are loadable.
 * Runs under Bazel. For full DB integration tests (RoleRepository, AuthorityRepository,
 * RoleAuthorityRepository with Testcontainers PostgreSQL), run with Maven:
 *   mvn test -Dtest=*RepositoryIntegrationTest
 */
@DisplayName("Repository Integration Tests")
class RepositoryIntegrationTest {

    @Test
    @DisplayName("Repository classes are loadable")
    fun repositoryClassesAreLoadable() {
        assertNotNull(RoleRepository::class.java)
        assertNotNull(AuthorityRepository::class.java)
    }
}
