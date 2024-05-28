package utils.com.gabriel.orders.infra;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class SimplePreAuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String scopeKey = httpRequest.getHeader("Authorization");

        if (scopeKey != null && scopeKey.startsWith("Bearer ")) {
            scopeKey = scopeKey.substring(7);

            // Mapeie o scopeKey para uma lista de autoridades
            List<SimpleGrantedAuthority> authorities = switch (scopeKey) {
                case "TOKENWITHSCOPEORDERSLIST" -> List.of(new SimpleGrantedAuthority("SCOPE_orders:list"));
                case "TOKENWITHSCOPEORDERSADD" -> List.of(new SimpleGrantedAuthority("SCOPE_orders:add"));
                case "TOKENWITHSCOPEORDERSVIEW" -> List.of(new SimpleGrantedAuthority("SCOPE_orders:view"));
                case "TOKENWITHSCOPEORDERSCANCEL" -> List.of(new SimpleGrantedAuthority("SCOPE_orders:cancel"));
                case "TOKENWITHSCOPEORDERSUPDATE" -> List.of(new SimpleGrantedAuthority("SCOPE_orders:update"));
                case "TOKENWITHWRONGSCOPE" -> List.of(new SimpleGrantedAuthority("SCOPE_wrong:scope"));
                default -> List.of();
            };

            // Criação do objeto PreAuthenticatedAuthenticationToken para o contexto de segurança
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(scopeKey, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
