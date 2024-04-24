package com.gabriel.permissions.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.permissions.domain.model.Authority;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.permissions.domain.model.RoleAuthorityKey;
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException;
import com.gabriel.permissions.domain.repository.AuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleAuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleRepository;
import com.gabriel.permissions.infraestructure.provider.Auth0Provider;
import jakarta.transaction.Transactional;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: add hierarchical roles
@Service
public class PermissionService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final String issuer;
    private final Auth0Provider auth0Provider;
    private final ObjectMapper objectMapper;

    public PermissionService(RoleRepository roleRepository, AuthorityRepository authorityRepository,
                             RoleAuthorityRepository roleAuthorityRepository,
                             @Value("${auth0.issuer}") String issuer,
                             Auth0Provider auth0Provider,
                             ObjectMapper objectMapper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.issuer = issuer;
        this.auth0Provider = auth0Provider;
        this.objectMapper = objectMapper;
    }


    public List<Role> retrieveAllRoles() {
        return roleRepository.findAll();
    }

    public Role retrieveRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
    }

    public Role retrieveRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + roleName));
    }

    public Set<RoleAuthority> retrieveRoleAuthoritiesByName(String roleName) {

        return roleRepository.findByName(roleName)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + roleName))
            .getRoleAuthorities();
    }

    public Set<RoleAuthority> retrieveRolesAuthoritiesByName(List<String> rolesNames) {
        return rolesNames.stream()
            .map(this::retrieveRoleAuthoritiesByName)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRoleGrantedAuthoritiesByName(String roleName) {
        return retrieveRoleAuthoritiesByName(roleName).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRolesGrantedAuthoritiesByName(List<String> rolesNames) {
        return retrieveRolesAuthoritiesByName(rolesNames).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }

    @Transactional
    public Role createRole(Role role) throws UnirestException {

        roleRepository.save(role);

        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.post(issuer + "/roles")
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(new JsonNode("{\"name\":\"" + role.getName() + "\", \"description\": \"" + role.getDescription() + "\"}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to create role");
        }

        return role;
    }

    @Transactional
    public Role updateRoleById(UUID roleId, Role roleDetails) throws UnirestException {

        Role roleToUpdate = retrieveRoleById(roleId);
        String roleProviderId = auth0Provider.getRoleIdFromName(roleToUpdate.getName());

        roleToUpdate.setName(roleDetails.getName());
        roleToUpdate.setDescription(roleDetails.getDescription());
        roleRepository.save(roleToUpdate);

        String token = auth0Provider.getManagementApiToken();
        HttpResponse<JsonNode> response = Unirest.patch(issuer + "/roles/" + roleProviderId)
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(new JsonNode("{\"name\":\"" + roleDetails.getName() + "\", \"description\": \"" + roleDetails.getDescription() + "\"}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to update role");
        }

        return roleToUpdate;
    }

    @Transactional
    public void deleteRoleById(UUID roleId) throws UnirestException {
        Role roleToDelete = retrieveRoleById(roleId);
        String roleProviderId = auth0Provider.getRoleIdFromName(roleToDelete.getName());

        roleRepository.deleteById(roleId);

        String token = auth0Provider.getManagementApiToken();
        HttpResponse<JsonNode> response = Unirest.delete(issuer + "/roles/" + roleProviderId)
            .header("Authorization", "Bearer " + token)
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to delete role");
        }
    }

    public List<Authority> listAuthorities() {
        return authorityRepository.findAll();
    }

    @Transactional
    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Transactional
    public Authority updateAuthorityById(UUID authorityId, Authority authorityDetails) throws UnirestException, JsonProcessingException {
        Authority authorityToUpdate = authorityRepository.findById(authorityId)
            .orElseThrow(() -> new RoleNotFoundException("Authority not found with id: " + authorityId));

        authorityToUpdate.setName(authorityDetails.getName());
        authorityToUpdate.setDescription(authorityDetails.getDescription());
        authorityRepository.save(authorityToUpdate);

        return authorityToUpdate;
    }

    @Transactional
    public void deleteAuthorityById(UUID authorityId) throws UnirestException {
        authorityRepository.deleteById(authorityId);
    }

    public List<String> listRoleAdmins() throws UnirestException, JsonProcessingException {
        String roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN");
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.get(issuer + "/api/v2/roles/" + roleId + "/users")
            .header("Authorization", "Bearer " + token)
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to list role admins");
        }

        return objectMapper.readValue(response.getBody().toString(), List.class);
    }

    public String addRoleAdmin(String userId) throws UnirestException {
        String roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN");
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.post(issuer + "/api/v2/roles/" + roleId + "/users")
            .header("Authorization", "Bearer " + token)
            .body(new JsonNode("{\"users\": [\"" + userId + "\"]}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to add role admin");
        }

        return roleId;
    }

    public void removeRoleAdmin(String userId) throws UnirestException {
        String roleId = auth0Provider.getRoleIdFromName("POSTECH_GROUP_ADMIN");
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.delete(issuer + "/api/v2/roles/" + roleId + "/users")
            .header("Authorization", "Bearer " + token)
            .body(new JsonNode("{\"users\": [\"" + userId + "\"]}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to remove role admin");
        }
    }

    public List<String> listRoleUsers(UUID roleId) throws UnirestException, JsonProcessingException {
        String roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).getName());
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.get(issuer + "/api/v2/roles/" + roleProviderId + "/users")
            .header("Authorization", "Bearer " + token)
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to list role users");
        }

        return objectMapper.readValue(response.getBody().toString(), List.class);
    }

    public String addRoleUser(UUID roleId, String userId) throws UnirestException {
        String roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).getName());
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.post(issuer + "/api/v2/roles/" + roleProviderId + "/users")
            .header("Authorization", "Bearer " + token)
            .body(new JsonNode("{\"users\": [\"" + userId + "\"]}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to add role user");
        }

        return roleProviderId;
    }

    public void removeRoleUser(UUID roleId, String userId) throws UnirestException {
        String roleProviderId = auth0Provider.getRoleIdFromName(retrieveRoleById(roleId).getName());
        String token = auth0Provider.getManagementApiToken();

        HttpResponse<JsonNode> response = Unirest.delete(issuer + "/api/v2/roles/" + roleProviderId + "/users")
            .header("Authorization", "Bearer " + token)
            .body(new JsonNode("{\"users\": [\"" + userId + "\"]}"))
            .asJson();

        if (!response.isSuccess()) {
            throw new RuntimeException("Failed to remove role user");
        }
    }

    public Set<RoleAuthority> listRoleAuthorities(UUID roleId) {
        Role role = retrieveRoleById(roleId);
        return role.getRoleAuthorities();
    }

    public RoleAuthority addRoleAuthority(UUID roleId, UUID authorityId) {
        RoleAuthority roleAuthority = new RoleAuthority();
        roleAuthority.setId(new RoleAuthorityKey(roleId, authorityId));
        roleAuthority.setUserId("admin");
        return roleAuthorityRepository.save(roleAuthority);
    }

    public void removeRoleAuthority(UUID roleId, UUID authorityId) {
        roleAuthorityRepository.deleteById(new RoleAuthorityKey(roleId, authorityId));
    }
}
