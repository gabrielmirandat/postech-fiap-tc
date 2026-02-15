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
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import kong.unirest.core.json.JSONObject
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ApplicationScoped
class PermissionService(
    private val roleRepository: RoleRepository,
    private val authorityRepository: AuthorityRepository,
    private val roleAuthorityRepository: RoleAuthorityRepository,
    @ConfigProperty(name = "auth0.issuer") private val issuer: String,
    private val auth0Provider: Auth0Provider,
    private val objectMapper: ObjectMapper
) {

    fun retrieveAllRoles(): List<Role> = roleRepository.listAll()

    fun retrieveRoleById(roleId: UUID): Role =
        roleRepository.findById(roleId) ?: throw RoleNotFoundException("Role not found with id: $roleId")

    fun retrieveRoleByName(roleName: String): Role =
        roleRepository.findByName(roleName) ?: throw RoleNotFoundException("Role not found with name: $roleName")

    @Transactional
    fun retrieveRoleAuthoritiesByName(roleName: String): Set<RoleAuthority> =
        (roleRepository.findByName(roleName) ?: throw RoleNotFoundException("Role not found with name: $roleName")).roleAuthorities

    @Transactional
    fun retrieveRolesAuthoritiesByName(rolesNames: List<String>): Set<RoleAuthority> =
        rolesNames.flatMap { retrieveRoleAuthoritiesByName(it) }.toSet()

    /** Returns authority names (e.g. "groups:list") for the given role names. Used by SecurityIdentityAugmentor. */
    @Transactional
    fun retrieveRolesAuthorityNamesByName(rolesNames: List<String>): Set<String> =
        retrieveRolesAuthoritiesByName(rolesNames).mapNotNull { it.authority?.name }.toSet()

    @Transactional
    fun createRole(role: Role): Role {
        roleRepository.persist(role)
        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(JsonNode("""{"name":"${role.name}", "description": "${role.description}"}"""))
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to create role")
        return role
    }

    @Transactional
    fun updateRoleById(roleId: UUID, roleDetails: Role): Role {
        val roleToUpdate = retrieveRoleById(roleId)
        val roleProviderId = auth0Provider.getRoleIdFromName(roleToUpdate.name!!)
        roleToUpdate.name = roleDetails.name
        roleToUpdate.description = roleDetails.description
        roleRepository.persist(roleToUpdate)
        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.patch("https://$issuer/api/v2/roles/$roleProviderId")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(JsonNode("""{"name":"${roleDetails.name}", "description": "${roleDetails.description}"}"""))
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to update role")
        return roleToUpdate
    }

    @Transactional
    fun deleteRoleById(roleId: UUID) {
        val roleToDelete = retrieveRoleById(roleId)
        val roleProviderId = auth0Provider.getRoleIdFromName(roleToDelete.name!!)
        roleRepository.deleteById(roleId)
        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.delete("https://$issuer/api/v2/roles/$roleProviderId")
            .header("Authorization", "Bearer $token")
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to delete role")
    }

    fun listAuthorities(): List<Authority> = authorityRepository.listAll()

    @Transactional
    fun createAuthority(authority: Authority): Authority {
        authorityRepository.persist(authority)
        return authority
    }

    @Transactional
    fun updateAuthorityById(authorityId: UUID, authorityDetails: Authority): Authority {
        val authorityToUpdate = authorityRepository.findById(authorityId)
            ?: throw RoleNotFoundException("Authority not found with id: $authorityId")
        authorityToUpdate.name = authorityDetails.name
        authorityToUpdate.description = authorityDetails.description
        authorityRepository.persist(authorityToUpdate)
        return authorityToUpdate
    }

    @Transactional
    fun deleteAuthorityById(authorityId: UUID) {
        authorityRepository.deleteById(authorityId)
    }

    fun listRoleAdmins(): List<String> {
        val roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN")
        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.get("https://$issuer/api/v2/roles/$roleId/users")
            .header("Authorization", "Bearer $token")
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to list role admins")
        return objectMapper.readValue(response.body.toString(), List::class.java) as List<String>
    }

    fun addRoleAdmin(userId: String): String {
        val roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN")
        val token = auth0Provider.getManagementApiToken()
        val payload = JSONObject().apply { put("users", arrayOf(userId)) }
        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles/$roleId/users")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to add role admin: ${response.body}")
        return roleId
    }

    fun removeRoleAdmin(userId: String) {
        val roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN")
        val token = auth0Provider.getManagementApiToken()
        val encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8)
        val payload = JSONObject().apply { put("roles", arrayOf(roleId)) }
        val response: HttpResponse<JsonNode> = Unirest.delete("https://$issuer/api/v2/users/$encodedUserId/roles")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to remove role admin: ${response.body}")
    }

    fun listRoleUsers(roleId: UUID): List<String> {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.get("https://$issuer/api/v2/roles/$roleProviderId/users")
            .header("Authorization", "Bearer $token")
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to list role users")
        return objectMapper.readValue(response.body.toString(), List::class.java) as List<String>
    }

    fun addRoleUser(roleId: UUID, userId: String): String {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()
        val payload = JSONObject().apply { put("users", arrayOf(userId)) }
        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles/$roleProviderId/users")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to add role user: ${response.body}")
        return roleProviderId
    }

    fun removeRoleUser(roleId: UUID, userId: String) {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()
        val encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8)
        val payload = JSONObject().apply { put("roles", arrayOf(roleProviderId)) }
        val response: HttpResponse<JsonNode> = Unirest.delete("https://$issuer/api/v2/users/$encodedUserId/roles")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()
        if (!response.isSuccess) throw RuntimeException("Failed to remove role user: ${response.body}")
    }

    fun listRoleAuthorities(roleId: UUID): Set<RoleAuthority> = retrieveRoleById(roleId).roleAuthorities

    @Transactional
    fun addRoleAuthority(roleId: UUID, authorityId: UUID): RoleAuthority {
        val permissionIdValue = generatePermissionId()
        val roleAuthority = RoleAuthority(
            key = RoleAuthorityKey(roleId, authorityId),
            permissionID = PermissionId.newBuilder().setValue(permissionIdValue).build(),
            role = retrieveRoleById(roleId),
            authority = retrieveAuthorityById(authorityId),
            userId = "admin"
        )
        roleAuthorityRepository.persist(roleAuthority)
        return roleAuthority
    }

    private fun generatePermissionId(): String {
        val part1 = UUID.randomUUID().toString().substring(0, 8)
        val part2 = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return "$part1-PERM-$part2"
    }

    @Transactional
    fun removeRoleAuthority(roleId: UUID, authorityId: UUID) {
        roleAuthorityRepository.deleteById(RoleAuthorityKey(roleId, authorityId))
    }

    fun retrieveAuthorityById(authorityId: UUID): Authority =
        authorityRepository.findById(authorityId)
            ?: throw RoleNotFoundException("Authority not found with id: $authorityId")
}
