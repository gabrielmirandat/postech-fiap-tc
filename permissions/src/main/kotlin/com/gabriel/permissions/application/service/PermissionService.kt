package com.gabriel.permissions.application.service

import com.fasterxml.jackson.core.JsonProcessingException
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
import jakarta.transaction.Transactional
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import kong.unirest.core.UnirestException
import kong.unirest.core.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: add hierarchical roles
@Service
class PermissionService(
    private val roleRepository: RoleRepository,
    private val authorityRepository: AuthorityRepository,
    private val roleAuthorityRepository: RoleAuthorityRepository,
    @Value("\${auth0.issuer}") private val issuer: String,
    private val auth0Provider: Auth0Provider,
    private val objectMapper: ObjectMapper
) {

    fun retrieveAllRoles(): List<Role> = roleRepository.findAll()

    fun retrieveRoleById(roleId: UUID): Role =
        roleRepository.findById(roleId)
            .orElseThrow { RoleNotFoundException("Role not found with id: $roleId") }

    fun retrieveRoleByName(roleName: String): Role =
        roleRepository.findByName(roleName)
            .orElseThrow { RoleNotFoundException("Role not found with name: $roleName") }

    @Transactional
    fun retrieveRoleAuthoritiesByName(roleName: String): Set<RoleAuthority> =
        roleRepository.findByName(roleName)
            .orElseThrow { RoleNotFoundException("Role not found with name: $roleName") }
            .roleAuthorities

    @Transactional
    fun retrieveRolesAuthoritiesByName(rolesNames: List<String>): Set<RoleAuthority> =
        rolesNames
            .flatMap { retrieveRoleAuthoritiesByName(it) }
            .toSet()

    @Transactional
    fun retrieveRoleGrantedAuthoritiesByName(roleName: String): Set<GrantedAuthority> =
        retrieveRoleAuthoritiesByName(roleName)
            .map { SimpleGrantedAuthority(it.authority!!.name) }
            .toSet()

    @Transactional
    fun retrieveRolesGrantedAuthoritiesByName(rolesNames: List<String>): Set<GrantedAuthority> =
        retrieveRolesAuthoritiesByName(rolesNames)
            .map { SimpleGrantedAuthority(it.authority!!.name) }
            .toSet()

    @Transactional
    fun createRole(role: Role): Role {
        roleRepository.save(role)

        val token = auth0Provider.getManagementApiToken()

        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(JsonNode("""{"name":"${role.name}", "description": "${role.description}"}"""))
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to create role")
        }

        return role
    }

    @Transactional
    fun updateRoleById(roleId: UUID, roleDetails: Role): Role {
        val roleToUpdate = retrieveRoleById(roleId)
        val roleProviderId = auth0Provider.getRoleIdFromName(roleToUpdate.name!!)

        roleToUpdate.apply {
            name = roleDetails.name
            description = roleDetails.description
        }
        roleRepository.save(roleToUpdate)

        val token = auth0Provider.getManagementApiToken()
        val response: HttpResponse<JsonNode> = Unirest.patch("https://$issuer/api/v2/roles/$roleProviderId")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(JsonNode("""{"name":"${roleDetails.name}", "description": "${roleDetails.description}"}"""))
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to update role")
        }

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

        if (!response.isSuccess) {
            throw RuntimeException("Failed to delete role")
        }
    }

    fun listAuthorities(): List<Authority> = authorityRepository.findAll()

    @Transactional
    fun createAuthority(authority: Authority): Authority = authorityRepository.save(authority)

    @Transactional
    fun updateAuthorityById(authorityId: UUID, authorityDetails: Authority): Authority {
        val authorityToUpdate = authorityRepository.findById(authorityId)
            .orElseThrow { RoleNotFoundException("Authority not found with id: $authorityId") }

        authorityToUpdate.apply {
            name = authorityDetails.name
            description = authorityDetails.description
        }

        return authorityRepository.save(authorityToUpdate)
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

        if (!response.isSuccess) {
            throw RuntimeException("Failed to list role admins")
        }

        return objectMapper.readValue(response.body.toString(), List::class.java) as List<String>
    }

    fun addRoleAdmin(userId: String): String {
        val roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN")
        val token = auth0Provider.getManagementApiToken()

        val payload = JSONObject().apply {
            put("users", arrayOf(userId))
        }

        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles/$roleId/users")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to add role admin: ${response.body}")
        }

        return roleId
    }

    fun removeRoleAdmin(userId: String) {
        val roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN")
        val token = auth0Provider.getManagementApiToken()

        val encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8)
        println("Encoded UserID: $encodedUserId")

        val payload = JSONObject().apply {
            put("roles", arrayOf(roleId))
        }

        val url = "https://$issuer/api/v2/users/$encodedUserId/roles"
        println("Request URL: $url")  // Debugging output

        val response: HttpResponse<JsonNode> = Unirest.delete(url)
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to remove role admin: ${response.body}")
        }
    }

    fun listRoleUsers(roleId: UUID): List<String> {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()

        val response: HttpResponse<JsonNode> = Unirest.get("https://$issuer/api/v2/roles/$roleProviderId/users")
            .header("Authorization", "Bearer $token")
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to list role users")
        }

        return objectMapper.readValue(response.body.toString(), List::class.java) as List<String>
    }

    fun addRoleUser(roleId: UUID, userId: String): String {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()

        val payload = JSONObject().apply {
            put("users", arrayOf(userId))
        }

        val response: HttpResponse<JsonNode> = Unirest.post("https://$issuer/api/v2/roles/$roleProviderId/users")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to add role admin: ${response.body}")
        }

        return roleProviderId
    }

    fun removeRoleUser(roleId: UUID, userId: String) {
        val roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).name!!)
        val token = auth0Provider.getManagementApiToken()

        val encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8)
        println("Encoded UserID: $encodedUserId")

        val payload = JSONObject().apply {
            put("roles", arrayOf(roleProviderId))
        }

        val url = "https://$issuer/api/v2/users/$encodedUserId/roles"
        println("Request URL: $url")  // Debugging output

        val response: HttpResponse<JsonNode> = Unirest.delete(url)
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .body(payload.toString())
            .asJson()

        if (!response.isSuccess) {
            throw RuntimeException("Failed to remove role admin: ${response.body}")
        }
    }

    fun listRoleAuthorities(roleId: UUID): Set<RoleAuthority> {
        val role = retrieveRoleById(roleId)
        return role.roleAuthorities
    }

    fun addRoleAuthority(roleId: UUID, authorityId: UUID): RoleAuthority {
        val permissionIdValue = generatePermissionId()
        val roleAuthority = RoleAuthority(
            RoleAuthorityKey(roleId, authorityId),
            PermissionId.newBuilder().setValue(permissionIdValue).build(),
            retrieveRoleById(roleId),
            retrieveAuthorityById(authorityId),
            "admin"
        )
        return roleAuthorityRepository.save(roleAuthority)
    }

    private fun generatePermissionId(): String {
        val part1 = UUID.randomUUID().toString().substring(0, 8)
        val part2 = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return "$part1-PERM-$part2"
    }

    fun removeRoleAuthority(roleId: UUID, authorityId: UUID) {
        roleAuthorityRepository.deleteById(RoleAuthorityKey(roleId, authorityId))
    }

    fun retrieveAuthorityById(authorityId: UUID): Authority =
        authorityRepository.findById(authorityId)
            .orElseThrow { RoleNotFoundException("Authority not found with id: $authorityId") }
}
