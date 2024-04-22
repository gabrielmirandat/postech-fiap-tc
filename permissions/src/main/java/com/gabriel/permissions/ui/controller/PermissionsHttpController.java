package com.gabriel.permissions.ui.controller;

import com.gabriel.permissions.application.service.PermissionService;
import com.gabriel.permissions.domain.model.Authority;
import com.gabriel.permissions.domain.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PermissionsHttpController {

    private final PermissionService permissionService;

    public PermissionsHttpController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // Groups Roles endpoints
    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('groups:view')")
    public ResponseEntity<?> listGroups() {
        return ResponseEntity.ok(permissionService.retrieveAllRoles());
    }

    @PostMapping("/groups")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<?> createGroup(@RequestBody Role role) {
        return ResponseEntity.ok(permissionService.createRole(role));
    }

    @PutMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<?> updateGroup(@PathVariable String roleId, @RequestBody Role role) {
        return ResponseEntity.ok(permissionService.updateRole(roleId, role));
    }

    @DeleteMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:remove')")
    public ResponseEntity<?> deleteGroup(@PathVariable String roleId) {
        permissionService.deleteRole(roleId);
        return ResponseEntity.ok("Group deleted successfully");
    }

    // Group Admins endpoints
    @GetMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:view')")
    public ResponseEntity<?> listGroupAdmins() {
        return ResponseEntity.ok(permissionService.listRoleAdmins());
    }

    @PostMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:add')")
    public ResponseEntity<?> addGroupAdmin(@RequestBody String userId) {
        return ResponseEntity.ok(permissionService.addRoleAdmin(userId));
    }

    @DeleteMapping("/group_admins/{adminId}")
    @PreAuthorize("hasAuthority('groups:admins:remove')")
    public ResponseEntity<?> removeGroupAdmin(@PathVariable String userId) {
        permissionService.removeRoleAdmin(userId);
        return ResponseEntity.ok("Group admin removed successfully");
    }

    // Scopes endpoints
    @GetMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:view')")
    public ResponseEntity<?> listScopes() {
        return ResponseEntity.ok(permissionService.listAuthorities());
    }

    @PostMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<?> createScope(@RequestBody Authority authority) {
        return ResponseEntity.ok(permissionService.createAuthority(authority));
    }

    @PutMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<?> updateScope(@PathVariable String authorityId, @RequestBody Authority authority) {
        return ResponseEntity.ok(permissionService.updateAuthority(authorityId, authority));
    }

    @DeleteMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:remove')")
    public ResponseEntity<?> deleteScope(@PathVariable String authorityId) {
        permissionService.deleteAuthority(authorityId);
        return ResponseEntity.ok("Scope removed successfully");
    }

    // Groups Users endpoints
    @GetMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:view')")
    public ResponseEntity<?> listGroupUsers(@PathVariable String roleId) {
        return ResponseEntity.ok(permissionService.listRoleUsers(roleId));
    }

    @PostMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:add')")
    public ResponseEntity<?> addGroupUser(@PathVariable String roleId, @RequestBody String userId) {
        return ResponseEntity.ok(permissionService.addRoleUser(roleId, userId));
    }

    @DeleteMapping("/groups/{groupId}/users/{userId}")
    @PreAuthorize("hasAuthority('groups:users:remove')")
    public ResponseEntity<?> removeGroupUser(@PathVariable String roleId, @PathVariable String userId) {
        permissionService.removeRoleUser(roleId, userId);
        return ResponseEntity.ok("User removed from group successfully");
    }

    // Groups Scopes endpoints
    @GetMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:view')")
    public ResponseEntity<?> listGroupScopes(@PathVariable String roleId) {
        return ResponseEntity.ok(permissionService.listRoleAuthorities(roleId));
    }

    @PostMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:add')")
    public ResponseEntity<?> addGroupScope(@PathVariable String roleId, @RequestBody String authority) {
        return ResponseEntity.ok(permissionService.addRoleAuthority(roleId, authority));
    }

    @DeleteMapping("/groups/{groupId}/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('groups:scopes:remove')")
    public ResponseEntity<?> removeGroupScope(@PathVariable String roleId, @PathVariable String authority) {
        permissionService.removeRoleAuthority(roleId, authority);
        return ResponseEntity.ok("Scope removed from group successfully");
    }
}
