package com.gabriel.permissions.container

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

/**
 * Quarkus test resource that starts a PostgreSQL container for integration tests.
 * Use with @QuarkusTestResource(PostgresTestResource::class) on integration test classes.
 */
class PostgresTestResource : QuarkusTestResourceLifecycleManager {

    companion object {
        private const val IMAGE = "postgres:16-alpine"
        private const val DATABASE = "permissions_test"
    }

    private var postgres: PostgreSQLContainer<*>? = null

    override fun start(): MutableMap<String, String> {
        postgres = PostgreSQLContainer(DockerImageName.parse(IMAGE))
            .withDatabaseName(DATABASE)
            .withUsername("test")
            .withPassword("test")
        postgres!!.start()

        return mutableMapOf(
            "quarkus.datasource.db-kind" to "postgresql",
            "quarkus.datasource.username" to postgres!!.username,
            "quarkus.datasource.password" to postgres!!.password,
            "quarkus.datasource.jdbc.url" to postgres!!.jdbcUrl,
            "quarkus.liquibase.migrate-at-start" to "true"
        )
    }

    override fun stop() {
        postgres?.stop()
    }
}
