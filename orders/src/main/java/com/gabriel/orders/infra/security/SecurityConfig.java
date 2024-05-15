package com.gabriel.orders.infra.security;

import com.gabriel.orders.core.domain.port.PermissionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final PermissionRepository permissionRepository;
    private final String authEndpointUrl;

    public SecurityConfig(PermissionRepository permissionRepository, @Value("${auth.endpoint.url}") String authEndpointUrl) {
        this.permissionRepository = permissionRepository;
        this.authEndpointUrl = authEndpointUrl;
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("postech_roles");
            if (roles == null) {
                return List.of();
            }
            return permissionRepository.allPermissions().stream()
                .filter(item -> roles.contains(item.getRoleName()))
                .map(item -> new SimpleGrantedAuthority(item.getAuthorityName()))
                .collect(Collectors.toSet());
        });

        return jwtConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation(authEndpointUrl);
    }
}