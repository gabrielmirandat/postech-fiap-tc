package com.gabriel.permissions.infraestructure.security

import com.gabriel.permissions.application.service.PermissionService
import io.quarkus.security.identity.AuthenticationRequestContext
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.security.identity.SecurityIdentityAugmentor
import io.quarkus.security.runtime.QuarkusSecurityIdentity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.jwt.JsonWebToken

@ApplicationScoped
class PermissionsSecurityAugmentor(
    private val permissionService: PermissionService
) : SecurityIdentityAugmentor {

    override fun augment(identity: SecurityIdentity, context: AuthenticationRequestContext): Uni<SecurityIdentity> {
        val jwt = identity.principal as? JsonWebToken ?: return Uni.createFrom().item(identity)
        val roleNames = runCatching { jwt.getClaim<Collection<String>>("postech_roles") }.getOrNull() ?: emptyList()
        val authorityNames = if (roleNames.isEmpty()) emptySet() else permissionService.retrieveRolesAuthorityNamesByName(roleNames.toList())
        val scopes = runCatching { jwt.getClaim<Collection<String>>("scope") }.getOrNull() ?: emptyList()
        val scopeRoles = scopes.map { "SCOPE_$it" }.toSet()
        val builder = QuarkusSecurityIdentity.builder(identity)
        (authorityNames + scopeRoles).forEach { builder.addRole(it) }
        return Uni.createFrom().item(builder.build())
    }
}
