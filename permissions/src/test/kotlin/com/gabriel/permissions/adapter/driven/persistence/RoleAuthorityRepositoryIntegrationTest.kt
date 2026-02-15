package com.gabriel.permissions.adapter.driven.persistence

import com.gabriel.model.PermissionId
import com.gabriel.permissions.container.PostgresTestResource
import com.gabriel.permissions.domain.model.Authority
import com.gabriel.permissions.domain.model.Role
import com.gabriel.permissions.domain.model.RoleAuthority
import com.gabriel.permissions.domain.model.RoleAuthorityKey
import com.gabriel.permissions.domain.repository.AuthorityRepository
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
@DisplayName("RoleAuthorityRepository integration tests")
class RoleAuthorityRepositoryIntegrationTest {

    @Inject
    lateinit var roleRepository: RoleRepository

    @Inject
    lateinit var authorityRepository: AuthorityRepository

    @Inject
    lateinit var roleAuthorityRepository: RoleAuthorityRepository

    private lateinit var role: Role
    private lateinit var authority: Authority

    @BeforeEach
    fun setUp() {
        roleAuthorityRepository.listAll().forEach { ra -> roleAuthorityRepository.deleteById(ra.key!!) }
        roleRepository.listAll().forEach { r -> roleRepository.deleteByName(r.name!!) }
        authorityRepository.listAll().forEach { a -> authorityRepository.deleteById(a.id!!) }

        role = Role(null, "TEST_ROLE", "Test role", null, null, mutableSetOf())
        authority = Authority(null, "test:permission", "Test permission", null, null, mutableSetOf())
        roleRepository.persist(role)
        authorityRepository.persist(authority)
    }

    @Test
    @DisplayName("persist and findById return saved role_authority")
    fun persistAndFindById() {
        val ra = RoleAuthority().apply {
            this.role = this@RoleAuthorityRepositoryIntegrationTest.role
            this.authority = this@RoleAuthorityRepositoryIntegrationTest.authority
            key = RoleAuthorityKey(this@RoleAuthorityRepositoryIntegrationTest.role.id!!, this@RoleAuthorityRepositoryIntegrationTest.authority.id!!)
            permissionID = PermissionId.newBuilder().setValue("perm-1").build()
            userId = "user1"
        }

        roleAuthorityRepository.persist(ra)

        val key = RoleAuthorityKey(role.id!!, authority.id!!)
        val found = roleAuthorityRepository.findById(key)
        assertThat(found).isNotNull
        assertThat(found!!.permissionID!!.value).isEqualTo("perm-1")
        assertThat(found.userId).isEqualTo("user1")
    }

    @Test
    @DisplayName("findById returns null when not exists")
    fun findByIdNotFound() {
        val key = RoleAuthorityKey(UUID.randomUUID(), UUID.randomUUID())
        val found = roleAuthorityRepository.findById(key)
        assertThat(found).isNull()
    }

    @Test
    @DisplayName("listAll returns all persisted role_authorities")
    fun listAll() {
        val ra1 = RoleAuthority().apply {
            this.role = this@RoleAuthorityRepositoryIntegrationTest.role
            this.authority = this@RoleAuthorityRepositoryIntegrationTest.authority
            key = RoleAuthorityKey(this@RoleAuthorityRepositoryIntegrationTest.role.id!!, this@RoleAuthorityRepositoryIntegrationTest.authority.id!!)
            permissionID = PermissionId.newBuilder().setValue("p1").build()
            userId = "u1"
        }
        roleAuthorityRepository.persist(ra1)

        val auth2 = Authority(null, "auth2", "Auth 2", null, null, mutableSetOf())
        authorityRepository.persist(auth2)
        val ra2 = RoleAuthority().apply {
            this.role = this@RoleAuthorityRepositoryIntegrationTest.role
            this.authority = auth2
            key = RoleAuthorityKey(this@RoleAuthorityRepositoryIntegrationTest.role.id!!, auth2.id!!)
            permissionID = PermissionId.newBuilder().setValue("p2").build()
            userId = "u2"
        }
        roleAuthorityRepository.persist(ra2)

        val all = roleAuthorityRepository.listAll()
        assertThat(all).hasSize(2)
        assertThat(all.map { it.permissionID!!.value }).containsExactlyInAnyOrder("p1", "p2")
    }

    @Test
    @DisplayName("deleteById removes role_authority")
    fun deleteById() {
        val ra = RoleAuthority().apply {
            this.role = this@RoleAuthorityRepositoryIntegrationTest.role
            this.authority = this@RoleAuthorityRepositoryIntegrationTest.authority
            key = RoleAuthorityKey(this@RoleAuthorityRepositoryIntegrationTest.role.id!!, this@RoleAuthorityRepositoryIntegrationTest.authority.id!!)
            permissionID = PermissionId.newBuilder().setValue("to-delete").build()
            userId = "user"
        }
        roleAuthorityRepository.persist(ra)

        val key = RoleAuthorityKey(role.id!!, authority.id!!)
        roleAuthorityRepository.deleteById(key)

        assertThat(roleAuthorityRepository.findById(key)).isNull()
    }
}
