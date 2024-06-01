package utils.com.gabriel.orders.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.adapter.api.exceptions.Unauthorized;
import com.gabriel.orders.adapter.driver.api.mapper.OrderMapper;
import com.gabriel.specs.orders.models.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class MockAuthenticationFilter extends GenericFilterBean {

    @NotNull
    private static TestingAuthenticationToken getTestingAuthenticationToken(String mockedAuth) {
        List<SimpleGrantedAuthority> authorities = switch (mockedAuth) {
            case "TOKENWITHSCOPEORDERSLIST" -> List.of(new SimpleGrantedAuthority("orders:list"));
            case "TOKENWITHSCOPEORDERSADD" -> List.of(new SimpleGrantedAuthority("orders:add"));
            case "TOKENWITHSCOPEORDERSVIEW" -> List.of(new SimpleGrantedAuthority("orders:view"));
            case "TOKENWITHSCOPEORDERSCANCEL" -> List.of(new SimpleGrantedAuthority("orders:cancel"));
            case "TOKENWITHSCOPEORDERSUPDATE" -> List.of(new SimpleGrantedAuthority("orders:update"));
            case "TOKENWITHWRONGSCOPE" -> List.of(new SimpleGrantedAuthority("wrong:scope"));
            default -> List.of();
        };

        return new TestingAuthenticationToken(mockedAuth, null, authorities);
    }

    private ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String mockedAuth = httpRequest.getHeader("Authorization");

        if (mockedAuth != null) {
            if (mockedAuth.startsWith("Bearer ")) {
                mockedAuth = mockedAuth.substring(7);
            }

            if (mockedAuth.equals("NONE")) {
                Unauthorized unauthorized = new Unauthorized("Unauthorized");
                ErrorResponse error = OrderMapper.toErrorResponse(unauthorized);
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write(objectMapper().writeValueAsString(error));
                return;
            }

            TestingAuthenticationToken authentication = getTestingAuthenticationToken(mockedAuth);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        chain.doFilter(request, response);
    }
}
