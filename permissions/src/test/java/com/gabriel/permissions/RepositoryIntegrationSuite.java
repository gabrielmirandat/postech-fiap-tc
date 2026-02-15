package com.gabriel.permissions;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit 5 suite for integration tests that run under Bazel.
 * Includes only the lightweight integration test (no Quarkus/DB).
 * Tests that use {@code @QuarkusTest} (RoleRepository, AuthorityRepository,
 * RoleAuthorityRepository) require QuarkusClassLoader and must be run with Maven:
 * {@code mvn test -Dtest=*RepositoryIntegrationTest}.
 */
@Suite
@SelectClasses(RepositoryIntegrationTest.class)
public class RepositoryIntegrationSuite {
}
