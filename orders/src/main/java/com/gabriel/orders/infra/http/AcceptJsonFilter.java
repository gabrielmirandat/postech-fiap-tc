package com.gabriel.orders.infra.http;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * Filter que adiciona automaticamente o header Accept: application/json
 * em todas as requisições, garantindo que o Spring Boot sempre retorne JSON
 * em vez de HTML para erros.
 */
@Component
@Order(1)
public class AcceptJsonFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        
        // Adiciona Accept: application/json se não estiver presente
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader == null || acceptHeader.isEmpty() || 
            (!acceptHeader.contains("application/json") && !acceptHeader.contains("*/*"))) {
            // Cria um wrapper que adiciona o header Accept
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("Accept".equalsIgnoreCase(name)) {
                        String original = super.getHeader(name);
                        if (original == null || original.isEmpty()) {
                            return "application/json";
                        }
                        // Se já tem algum Accept, adiciona application/json com maior prioridade
                        if (!original.contains("application/json")) {
                            return "application/json, " + original;
                        }
                    }
                    return super.getHeader(name);
                }
                
                @Override
                public java.util.Enumeration<String> getHeaders(String name) {
                    if ("Accept".equalsIgnoreCase(name)) {
                        String header = getHeader(name);
                        return java.util.Collections.enumeration(java.util.Collections.singletonList(header));
                    }
                    return super.getHeaders(name);
                }
            };
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
