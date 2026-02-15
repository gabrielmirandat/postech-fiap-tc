package com.gabriel.permissions.ui.controller

import com.gabriel.permissions.application.service.PermissionService
import com.gabriel.permissions.ui.controller.request.GroupRequest
import com.gabriel.permissions.ui.controller.request.ScopeGroupRequest
import com.gabriel.permissions.ui.controller.request.ScopeRequest
import com.gabriel.permissions.ui.controller.request.UserGroupRequest
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.nio.charset.StandardCharsets
import java.util.*

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PermissionsHttpController(
    private val permissionService: PermissionService
) {

    @GET
    @Path("/groups")
    @RolesAllowed("groups:list")
    fun listGroups(): Response = Response.ok(permissionService.retrieveAllRoles()).build()

    @POST
    @Path("/groups")
    @RolesAllowed("groups:manage")
    fun createGroup(groupRequest: GroupRequest): Response =
        Response.ok(permissionService.createRole(groupRequest.toRole())).build()

    @PUT
    @Path("/groups/{groupId}")
    @RolesAllowed("groups:manage")
    fun updateGroup(@PathParam("groupId") groupId: UUID, groupRequest: GroupRequest): Response =
        Response.ok(permissionService.updateRoleById(groupId, groupRequest.toRole())).build()

    @DELETE
    @Path("/groups/{groupId}")
    @RolesAllowed("groups:remove")
    fun deleteGroup(@PathParam("groupId") groupId: UUID): Response {
        permissionService.deleteRoleById(groupId)
        return Response.ok("Group deleted successfully").build()
    }

    @GET
    @Path("/group_admins")
    @RolesAllowed("groups:admins:list")
    fun listGroupAdmins(): Response = Response.ok(permissionService.listRoleAdmins()).build()

    @POST
    @Path("/group_admins")
    @RolesAllowed("groups:admins:add")
    fun addGroupAdmin(userGroupRequest: UserGroupRequest): Response =
        Response.ok(permissionService.addRoleAdmin(userGroupRequest.userId)).build()

    @DELETE
    @Path("/group_admins/{base64adminId}")
    @RolesAllowed("groups:admins:remove")
    fun removeGroupAdmin(@PathParam("base64adminId") base64adminId: String): Response {
        val decodedAdminId = String(Base64.getDecoder().decode(base64adminId), StandardCharsets.UTF_8)
        permissionService.removeRoleAdmin(decodedAdminId)
        return Response.ok("Group admin removed successfully").build()
    }

    @GET
    @Path("/groups/{groupId}/users")
    @RolesAllowed("groups:users:list")
    fun listGroupUsers(@PathParam("groupId") groupId: UUID): Response =
        Response.ok(permissionService.listRoleUsers(groupId)).build()

    @POST
    @Path("/groups/{groupId}/users")
    @RolesAllowed("groups:users:add")
    fun addGroupUser(@PathParam("groupId") groupId: UUID, userGroupRequest: UserGroupRequest): Response =
        Response.ok(permissionService.addRoleUser(groupId, userGroupRequest.userId)).build()

    @DELETE
    @Path("/groups/{groupId}/users/{base64adminId}")
    @RolesAllowed("groups:users:remove")
    fun removeGroupUser(@PathParam("groupId") groupId: UUID, @PathParam("base64adminId") base64adminId: String): Response {
        val decodedAdminId = String(Base64.getDecoder().decode(base64adminId), StandardCharsets.UTF_8)
        permissionService.removeRoleUser(groupId, decodedAdminId)
        return Response.ok("User removed from group successfully").build()
    }

    @GET
    @Path("/groups/{groupId}/scopes")
    @RolesAllowed("groups:scopes:list")
    fun listGroupScopes(@PathParam("groupId") groupId: UUID): Response =
        Response.ok(permissionService.listRoleAuthorities(groupId)).build()

    @POST
    @Path("/groups/{groupId}/scopes")
    @RolesAllowed("groups:scopes:add")
    fun addGroupScope(@PathParam("groupId") groupId: UUID, scopeGroupRequest: ScopeGroupRequest): Response =
        Response.ok(permissionService.addRoleAuthority(groupId, scopeGroupRequest.authorityId)).build()

    @DELETE
    @Path("/groups/{groupId}/scopes/{scopeId}")
    @RolesAllowed("groups:scopes:remove")
    fun removeGroupScope(@PathParam("groupId") groupId: UUID, @PathParam("scopeId") scopeId: UUID): Response {
        permissionService.removeRoleAuthority(groupId, scopeId)
        return Response.ok("Scope removed from group successfully").build()
    }

    @GET
    @Path("/scopes")
    @RolesAllowed("scopes:list")
    fun listScopes(): Response = Response.ok(permissionService.listAuthorities()).build()

    @POST
    @Path("/scopes")
    @RolesAllowed("scopes:manage")
    fun createScope(scopeRequest: ScopeRequest): Response =
        Response.ok(permissionService.createAuthority(scopeRequest.toAuthority())).build()

    @PUT
    @Path("/scopes/{scopeId}")
    @RolesAllowed("scopes:manage")
    fun updateScope(@PathParam("scopeId") scopeId: UUID, scopeRequest: ScopeRequest): Response =
        Response.ok(permissionService.updateAuthorityById(scopeId, scopeRequest.toAuthority())).build()

    @DELETE
    @Path("/scopes/{scopeId}")
    @RolesAllowed("scopes:remove")
    fun deleteScope(@PathParam("scopeId") scopeId: UUID): Response {
        permissionService.deleteAuthorityById(scopeId)
        return Response.ok("Scope removed successfully").build()
    }
}
