package com.gabriel.permissions.adapter.driven.persistence

import com.gabriel.permissions.container.PostgresTestResource
import com.gabriel.permissions.domain.model.Authority
import com.gabriel.permissions.domain.repository.AuthorityRepository
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
@DisplayName("AuthorityRepository integration tests")
class AuthorityRepositoryIntegrationTest {

    @Inject
    lateinit var authorityRepository: AuthorityRepository

    @BeforeEach
    fun setUp() {
        authorityRepository.listAll().forEach { a -> authorityRepository.deleteById(a.id!!) }
    }

    @Test
    @DisplayName("persist and findById return saved authority")
    fun persistAndFindById() {
        val authority = Authority(null, "groups:list", "List groups", null, null, mutableSetOf())
        authorityRepository.persist(authority)

        assertThat(authority.id).isNotNull
        val found = authorityRepository.findById(authority.id!!)
        assertThat(found).isNotNull
        assertThat(found!!.name).isEqualTo("groups:list")
        assertThat(found.description).isEqualTo("List groups")
    }

    @Test
    @DisplayName("findById returns null when not exists")
    fun findByIdNotFound() {
        val found = authorityRepository.findById(UUID.randomUUID())
        assertThat(found).isNull()
    }

    @Test
    @DisplayName("listAll returns all persisted authorities")
    fun listAll() {
        authorityRepository.persist(Authority(null, "auth1", "Auth 1", null, null, mutableSetOf()))
        authorityRepository.persist(Authority(null, "auth2", "Auth 2", null, null, mutableSetOf()))

        val all = authorityRepository.listAll()
        assertThat(all).hasSize(2)
        assertThat(all.map { it.name }).containsExactlyInAnyOrder("auth1", "auth2")
    }

    @Test
    @DisplayName("deleteById removes authority")
    fun deleteById() {
        val authority = Authority(null, "to_delete", "To delete", null, null, mutableSetOf())
        authorityRepository.persist(authority)
        val id = authority.id!!

        authorityRepository.deleteById(id)

        assertThat(authorityRepository.findById(id)).isNull()
    }
}
