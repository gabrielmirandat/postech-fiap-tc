package com.gabriel.permissions.ui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.permissions.application.service.PermissionService;
import com.gabriel.permissions.ui.controller.request.GroupRequest;
import com.gabriel.permissions.ui.controller.request.ScopeGroupRequest;
import com.gabriel.permissions.ui.controller.request.ScopeRequest;
import com.gabriel.permissions.ui.controller.request.UserGroupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
public class PermissionsHttpController {

    private final PermissionService permissionService;

    public PermissionsHttpController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // Groups Roles endpoints
    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('groups:list')")
    public ResponseEntity<?> listGroups() {
        return ResponseEntity.ok(permissionService.retrieveAllRoles());
    }

    @PostMapping("/groups")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest) {
        return ResponseEntity.ok(permissionService.createRole(groupRequest.toRole()));
    }

    @PutMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<?> updateGroup(@PathVariable UUID groupId, @RequestBody GroupRequest groupRequest) {
        return ResponseEntity.ok(permissionService.updateRoleById(groupId, groupRequest.toRole()));
    }

    @DeleteMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:remove')")
    public ResponseEntity<?> deleteGroup(@PathVariable UUID groupId) {
        permissionService.deleteRoleById(groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }

    // Group Admins endpoints
    @GetMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:list')")
    public ResponseEntity<?> listGroupAdmins() throws JsonProcessingException {
        return ResponseEntity.ok(permissionService.listRoleAdmins());
    }

    @PostMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:add')")
    public ResponseEntity<?> addGroupAdmin(@RequestBody UserGroupRequest userGroupRequest) {
        return ResponseEntity.ok(permissionService.addRoleAdmin(userGroupRequest.userId()));
    }

    @DeleteMapping("/group_admins/{base64adminId}")
    @PreAuthorize("hasAuthority('groups:admins:remove')")
    public ResponseEntity<?> removeGroupAdmin(@PathVariable String base64adminId) {
        permissionService.removeRoleAdmin(new String(Base64.getDecoder().decode(base64adminId),
            StandardCharsets.UTF_8));
        return ResponseEntity.ok("Group admin removed successfully");
    }

    // Groups Users endpoints
    @GetMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:list')")
    public ResponseEntity<?> listGroupUsers(@PathVariable UUID groupId) throws JsonProcessingException {
        return ResponseEntity.ok(permissionService.listRoleUsers(groupId));
    }

    @PostMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:add')")
    public ResponseEntity<?> addGroupUser(@PathVariable UUID groupId, @RequestBody UserGroupRequest userGroupRequest) {
        return ResponseEntity.ok(permissionService.addRoleUser(groupId, userGroupRequest.userId()));
    }

    @DeleteMapping("/groups/{groupId}/users/{base64adminId}")
    @PreAuthorize("hasAuthority('groups:users:remove')")
    public ResponseEntity<?> removeGroupUser(@PathVariable UUID groupId, @PathVariable String base64adminId) {
        permissionService.removeRoleUser(groupId, new String(Base64.getDecoder().decode(base64adminId),
            StandardCharsets.UTF_8));
        return ResponseEntity.ok("User removed from group successfully");
    }

    // Groups Scopes endpoints
    @GetMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:list')")
    public ResponseEntity<?> listGroupScopes(@PathVariable UUID groupId) {
        return ResponseEntity.ok(permissionService.listRoleAuthorities(groupId));
    }

    @PostMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:add')")
    public ResponseEntity<?> addGroupScope(@PathVariable UUID groupId, @RequestBody ScopeGroupRequest scopeGroupRequest) {
        return ResponseEntity.ok(permissionService.addRoleAuthority(groupId, scopeGroupRequest.authorityId()));
    }

    @DeleteMapping("/groups/{groupId}/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('groups:scopes:remove')")
    public ResponseEntity<?> removeGroupScope(@PathVariable UUID groupId, @PathVariable UUID scopeId) {
        permissionService.removeRoleAuthority(groupId, scopeId);
        return ResponseEntity.ok("Scope removed from group successfully");
    }

    // Scopes endpoints
    @GetMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:list')")
    public ResponseEntity<?> listScopes() {
        return ResponseEntity.ok(permissionService.listAuthorities());
    }

    @PostMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<?> createScope(@RequestBody ScopeRequest scopeRequest) {
        return ResponseEntity.ok(permissionService.createAuthority(scopeRequest.toAuthority()));
    }

    @PutMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<?> updateScope(@PathVariable UUID scopeId, @RequestBody ScopeRequest scopeRequest) throws JsonProcessingException {
        return ResponseEntity.ok(permissionService.updateAuthorityById(scopeId, scopeRequest.toAuthority()));
    }

    @DeleteMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:remove')")
    public ResponseEntity<?> deleteScope(@PathVariable UUID scopeId) {
        permissionService.deleteAuthorityById(scopeId);
        return ResponseEntity.ok("Scope removed successfully");
    }
}
