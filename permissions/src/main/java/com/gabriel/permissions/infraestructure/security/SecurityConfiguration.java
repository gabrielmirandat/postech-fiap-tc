package com.gabriel.permissions.infraestructure.security;

import com.gabriel.permissions.application.service.PermissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfiguration {

    private final PermissionService permissionService;
    private final String issuer;

    public SecurityConfiguration(PermissionService permissionService, @Value("${auth0.issuer}") String issuer) {
        this.permissionService = permissionService;
        this.issuer = issuer;
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/users/login", "/users/register").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final List<String> roles = Optional.ofNullable(jwt.getClaimAsStringList("postech_roles")).orElse(List.of());
            final Set<GrantedAuthority> roleAuthorities = roles.isEmpty()
                ? Set.of()
                : permissionService.retrieveRolesGrantedAuthoritiesByName(roles);

            final List<String> scopes = Optional.ofNullable(jwt.getClaimAsStringList("scope")).orElse(List.of());
            final Set<GrantedAuthority> scopeAuthorities = scopes.stream()
                .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                .collect(Collectors.toSet());

            final Set<GrantedAuthority> combinedAuthorities = new HashSet<>();
            combinedAuthorities.addAll(roleAuthorities);
            combinedAuthorities.addAll(scopeAuthorities);

            return combinedAuthorities;
        });

        return jwtConverter;
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation("https://" + issuer + "/");
    }
}
