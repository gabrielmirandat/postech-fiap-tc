package com.gabriel.permissions.ui.controller;

import com.gabriel.permissions.application.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PermissionsHttpController {

    private PermissionService permissionService;

    // Groups Roles endpoints
    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('groups:view')")
    public ResponseEntity<String> listGroups() {
        return ResponseEntity.ok("VIEW_GROUPS - list all group roles");
    }

    @PostMapping("/groups")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<String> createGroup(@RequestBody String groupData) {
        return ResponseEntity.ok("MANAGE_GROUPS - create new group role");
    }

    @PutMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:manage')")
    public ResponseEntity<String> updateGroup(@PathVariable String groupId, @RequestBody String groupData) {
        return ResponseEntity.ok("MANAGE_GROUPS - update group role with id " + groupId);
    }

    @DeleteMapping("/groups/{groupId}")
    @PreAuthorize("hasAuthority('groups:remove')")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupId) {
        return ResponseEntity.ok("REMOVE_GROUPS - delete group role with id " + groupId);
    }

    // Group Admins endpoints
    @GetMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:view')")
    public ResponseEntity<String> listGroupAdmins() {
        return ResponseEntity.ok("VIEW_GROUP_ADMINS - list all group admins");
    }

    @PostMapping("/group_admins")
    @PreAuthorize("hasAuthority('groups:admins:add')")
    public ResponseEntity<String> createGroupAdmin(@RequestBody String groupAdminData) {
        return ResponseEntity.ok("ADD_GROUP_ADMINS - create new group admin");
    }

    @DeleteMapping("/group_admins/{adminId}")
    @PreAuthorize("hasAuthority('groups:admins:remove')")
    public ResponseEntity<String> deleteGroupAdmin(@PathVariable String adminId) {
        return ResponseEntity.ok("REMOVE_GROUP_ADMINS - delete group admin with id " + adminId);
    }

    // Scopes endpoints
    @GetMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:view')")
    public ResponseEntity<String> listScopes() {
        return ResponseEntity.ok("VIEW_SCOPES - list all scopes");
    }

    @PostMapping("/scopes")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<String> createScope(@RequestBody String scopeData) {
        return ResponseEntity.ok("MANAGE_SCOPES - create or update scope");
    }

    @PutMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:manage')")
    public ResponseEntity<String> updateScope(@PathVariable String scopeId, @RequestBody String scopeData) {
        return ResponseEntity.ok("MANAGE_SCOPES - create or update scope with id " + scopeId);
    }

    @DeleteMapping("/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('scopes:remove')")
    public ResponseEntity<String> removeScope(@PathVariable String scopeId) {
        return ResponseEntity.ok("REMOVE_SCOPES - remove scope with id " + scopeId);
    }

    // Groups Users endpoints
    @GetMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:view')")
    public ResponseEntity<String> listGroupUsers(@PathVariable String groupId) {
        return ResponseEntity.ok("VIEW_GROUP_USERS - list all users from group with id " + groupId);
    }

    @PostMapping("/groups/{groupId}/users")
    @PreAuthorize("hasAuthority('groups:users:add')")
    public ResponseEntity<String> addGroupUser(@PathVariable String groupId, @RequestBody String userData) {
        return ResponseEntity.ok("ADD_GROUP_USERS - add user to group with id " + groupId);
    }

    @DeleteMapping("/groups/{groupId}/users/{userId}")
    @PreAuthorize("hasAuthority('groups:users:remove')")
    public ResponseEntity<String> removeGroupUser(@PathVariable String groupId, @PathVariable String userId) {
        return ResponseEntity.ok("REMOVE_GROUP_USERS - remove user with id " + userId + " from group with id " + groupId);
    }

    // Groups Scopes endpoints
    @GetMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:view')")
    public ResponseEntity<String> listGroupScopes(@PathVariable String groupId) {
        return ResponseEntity.ok("VIEW_GROUP_SCOPES - list all scopes from group with id " + groupId);
    }

    @PostMapping("/groups/{groupId}/scopes")
    @PreAuthorize("hasAuthority('groups:scopes:add')")
    public ResponseEntity<String> addGroupScope(@PathVariable String groupId, @RequestBody String scopeData) {
        return ResponseEntity.ok("ADD_GROUP_SCOPES - add scope to group with id " + groupId);
    }

    @DeleteMapping("/groups/{groupId}/scopes/{scopeId}")
    @PreAuthorize("hasAuthority('groups:scopes:remove')")
    public ResponseEntity<String> removeGroupScope(@PathVariable String groupId, @PathVariable String scopeId) {
        return ResponseEntity.ok("REMOVE_GROUP_SCOPES - remove scope with id " + scopeId + " from group with id " + groupId);
    }

    // Orders endpoints for Order Admin and Squad
    @PostMapping("/orders/{orderId}/cancel")
    @PreAuthorize("hasAuthority('orders:cancel')")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok("CANCEL_ORDERS - cancel order with id " + orderId);
    }

    @PutMapping("/orders/{orderId}/update")
    @PreAuthorize("hasAuthority('orders:update')")
    public ResponseEntity<String> updateOrder(@PathVariable String orderId, @RequestBody String orderData) {
        return ResponseEntity.ok("UPDATE_ORDERS - update order with id " + orderId);
    }

    // Menu endpoints for Menu Admin and Squad
    @PostMapping("/menu/items")
    @PreAuthorize("hasAuthority('menu:add')")
    public ResponseEntity<String> addMenuItem(@RequestBody String menuItemData) {
        return ResponseEntity.ok("ADD_MENU_ITEMS - add new menu item");
    }

    @DeleteMapping("/menu/items/{itemId}")
    @PreAuthorize("hasAuthority('menu:remove')")
    public ResponseEntity<String> removeMenuItem(@PathVariable String itemId) {
        return ResponseEntity.ok("REMOVE_MENU_ITEMS - remove menu item with id " + itemId);
    }

    @PutMapping("/menu/items/{itemId}")
    @PreAuthorize("hasAuthority('menu:update')")
    public ResponseEntity<String> updateMenuItem(@PathVariable String itemId, @RequestBody String menuItemData) {
        return ResponseEntity.ok("UPDATE_MENU_ITEMS - update menu item with id " + itemId);
    }
}

