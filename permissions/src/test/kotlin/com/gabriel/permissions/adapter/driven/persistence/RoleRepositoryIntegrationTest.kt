package com.gabriel.permissions.adapter.driven.persistence

import com.gabriel.permissions.container.PostgresTestResource
import com.gabriel.permissions.domain.model.Role
import com.gabriel.permissions.domain.repository.RoleAuthorityRepository
import com.gabriel.permissions.domain.repository.RoleRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.UUID
import jakarta.inject.Inject
import jakarta.transaction.Transactional

@QuarkusTest
@QuarkusTestResource(PostgresTestResource::class)
@Transactional
@DisplayName("RoleRepository integration tests")
class RoleRepositoryIntegrationTest {

    @Inject
    lateinit var roleRepository: RoleRepository

    @Inject
    lateinit var roleAuthorityRepository: RoleAuthorityRepository

    @BeforeEach
    fun setUp() {
        roleAuthorityRepository.listAll().forEach { ra -> roleAuthorityRepository.deleteById(ra.key!!) }
        roleRepository.listAll().forEach { r -> roleRepository.deleteByName(r.name!!) }
    }

    @Test
    @DisplayName("persist and findById return saved role")
    fun persistAndFindById() {
        val role = Role(null, "ADMIN", "Administrator role", null, null, mutableSetOf())
        roleRepository.persist(role)

        assertThat(role.id).isNotNull
        val found = roleRepository.findById(role.id!!)
        assertThat(found).isNotNull
        assertThat(found!!.name).isEqualTo("ADMIN")
        assertThat(found.description).isEqualTo("Administrator role")
    }

    @Test
    @DisplayName("findByName returns role when exists")
    fun findByName() {
        val role = Role(null, "MANAGER", "Manager role", null, null, mutableSetOf())
        roleRepository.persist(role)

        val found = roleRepository.findByName("MANAGER")
        assertThat(found).isNotNull
        assertThat(found!!.id).isEqualTo(role.id)
    }

    @Test
    @DisplayName("findByName returns null when not exists")
    fun findByNameNotFound() {
        val found = roleRepository.findByName("NON_EXISTENT")
        assertThat(found).isNull()
    }

    @Test
    @DisplayName("listAll returns all persisted roles")
    fun listAll() {
        roleRepository.persist(Role(null, "R1", "Role 1", null, null, mutableSetOf()))
        roleRepository.persist(Role(null, "R2", "Role 2", null, null, mutableSetOf()))

        val all = roleRepository.listAll()
        assertThat(all).hasSize(2)
        assertThat(all.map { it.name }).containsExactlyInAnyOrder("R1", "R2")
    }

    @Test
    @DisplayName("deleteById removes role")
    fun deleteById() {
        val role = Role(null, "TO_DELETE", "To delete", null, null, mutableSetOf())
        roleRepository.persist(role)
        val id = role.id!!

        roleRepository.deleteById(id)

        assertThat(roleRepository.findById(id)).isNull()
    }

    @Test
    @DisplayName("deleteByName removes role")
    fun deleteByName() {
        val role = Role(null, "TO_DELETE_BY_NAME", "To delete", null, null, mutableSetOf())
        roleRepository.persist(role)

        roleRepository.deleteByName("TO_DELETE_BY_NAME")

        assertThat(roleRepository.findByName("TO_DELETE_BY_NAME")).isNull()
    }
}
