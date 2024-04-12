package com.gabriel.permissions.ui.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@SpringBootApplication
@RestController
public class OwnerController {

    @GetMapping("/hello-oauth")
    public String hello(Principal principal) {
        return "Hello, " + principal.getName();
    }

    @GetMapping("authorization-code/callback")
    public String redirect(Principal principal) {
        return "Hello, " + principal.getName();
    }

    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('groups:read')")
    public String listGroups() {
        return "List all groups";
    }

    @PostMapping("/groups")
    @PreAuthorize("hasAuthority('groups:write')")
    public String createGroup(@RequestBody String groupData) {
        return "Create new group";
    }

    @GetMapping("/group-admins")
    @PreAuthorize("hasAuthority('group_admins:read')")
    public String listGroupAdmins() {
        return "List all group admins";
    }

    @PostMapping("/group-admins")
    @PreAuthorize("hasAuthority('group_admins:write')")
    public String createGroupAdmin(@RequestBody String groupAdminData) {
        return "Create new group admin";
    }

    @DeleteMapping("/group-admins")
    @PreAuthorize("hasAuthority('group_admins:delete')")
    public String deleteGroupAdmin(@RequestBody String groupAdminData) {
        return "Delete group admin";
    }
}
