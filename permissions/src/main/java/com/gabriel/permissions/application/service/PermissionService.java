package com.gabriel.permissions.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.permissions.domain.model.exceptions.RoleNotFoundException;
import com.gabriel.permissions.domain.repository.AuthorityRepository;
import com.gabriel.permissions.domain.repository.RoleRepository;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: add hierarchical roles
@Service
public class PermissionService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private String issuer;
    private String clientId;
    private String clientSecret;
    private String applicationId;
    private String applicationSecret;
    private String audience;
    private String scope;
    private String applicationScope;
    private String logoutRedirectUrl;
    private ObjectMapper objectMapper;

    public PermissionService(RoleRepository roleRepository, AuthorityRepository authorityRepository,
                             @Value("${auth0.issuer}") String issuer,
                             @Value("${auth0.clientId}") String clientId,
                             @Value("${auth0.clientSecret}") String clientSecret,
                             @Value("${auth0.applicationId}") String applicationId,
                             @Value("${auth0.applicationSecret}") String applicationSecret,
                             @Value("${auth0.audience}") String audience,
                             @Value("${auth0.scope}") String scope,
                             @Value("${auth0.applicationScope}") String applicationScope,
                             @Value("${auth0.logout_url}") String logoutRedirectUrl,
                             ObjectMapper objectMapper) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.issuer = issuer;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.applicationId = applicationId;
        this.applicationSecret = applicationSecret;
        this.audience = audience;
        this.scope = scope;
        this.applicationScope = applicationScope;
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.objectMapper = objectMapper;
    }

    public List<Role> retrieveAllRoles() {
        return roleRepository.findAll();
    }

    public Role retrieveRole(String role) {
        return roleRepository.findByName(role)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role));
    }

    public Set<RoleAuthority> retrieveRoleAuthorities(String role) {

        return roleRepository.findByName(role)
            .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role))
            .getRoleAuthorities();
    }

    public Set<RoleAuthority> retrieveRolesAuthorities(List<String> roles) {
        return roles.stream()
            .map(this::retrieveRoleAuthorities)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRoleGrantedAuthorities(String role) {
        return retrieveRoleAuthorities(role).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }

    public Set<GrantedAuthority> retrieveRolesGrantedAuthorities(List<String> roles) {
        return retrieveRolesAuthorities(roles).stream()
            .map(roleAuthority -> new SimpleGrantedAuthority(roleAuthority.getAuthority().getName()))
            .collect(Collectors.toSet());
    }

    public Role createRole(Role role) throws UnirestException {
        String token = getManagementApiToken();
        HttpResponse<JsonNode> response = Unirest.post(issuer + "/roles")
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(new JsonNode("{\"name\":\"" + role.getName() + "\", \"description\": \"" + role.getDescription() + "\"}"))
            .asJson();

        if (response.isSuccess()) {
            try {
                return roleRepository.save(role);
            } catch (Exception e) {
                // TODO: Rollback Auth0 creation
                throw new RuntimeException("Failed to create role in Auth0");
            }
        } else {
            throw new RuntimeException("Failed to create role in Auth0");
        }
    }

    public Role updateRole(String roleName, Role roleDetails) throws UnirestException {
        String token = getManagementApiToken();
        Role roleToUpdate = retrieveRole(roleName);
        HttpResponse<JsonNode> response = Unirest.patch(issuer + "/roles/" + roleToUpdate.getId())
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .body(new JsonNode("{\"name\":\"" + roleDetails.getName() + "\", \"description\": \"" + roleDetails.getDescription() + "\"}"))
            .asJson();

        if (response.isSuccess()) {
            try {
                roleToUpdate.setName(roleDetails.getName());
                roleToUpdate.setDescription(roleDetails.getDescription());
                return roleRepository.save(roleToUpdate);
            } catch (Exception e) {
                // TODO: Rollback Auth0 update
                throw new RuntimeException("Failed to update role in Auth0");
            }
        } else {
            throw new RuntimeException("Failed to update role in Auth0");
        }
    }

    public void deleteRole(String roleName) throws UnirestException {
        Role roleToDelete = retrieveRole(roleName);
        String token = getManagementApiToken();
        HttpResponse<JsonNode> response = Unirest.delete(issuer + "/roles/" + roleToDelete.getId())
            .header("Authorization", "Bearer " + token)
            .asJson();

        if (response.isSuccess()) {
            try {
                roleRepository.delete(roleToDelete);
            } catch (Exception e) {
                // TODO: Rollback Auth0 update
                throw new RuntimeException("Failed to delete role in Auth0");
            }
        } else {
            throw new RuntimeException("Failed to delete role in Auth0");
        }
    }

    public String getManagementApiToken() throws UnirestException {
        HttpResponse<String> response = Unirest.post("https://" + issuer + "/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .field("grant_type", "client_credentials")
            .field("client_id", applicationId)
            .field("client_secret", applicationSecret)
            .field("audience", "https://" + issuer + "/api/v2/")
            .field("scope", applicationScope)
            .asString();

        if (response.isSuccess()) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Failed to obtain management API token.");
        }
    }
}
