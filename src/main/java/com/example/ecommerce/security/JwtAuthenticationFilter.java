package com.example.ecommerce.security;

import com.example.ecommerce.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Get Authorization header
        final String authHeader =
                request.getHeader("Authorization");

        // 2. No JWT provided
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract JWT
        final String jwt = authHeader.substring(7);

        try {

            // 4. Extract username/email from JWT
            String userEmail =
                    jwtService.extractUsername(jwt);

            // 5. Extract role from JWT
            String role =
                    jwtService.extractRole(jwt);

            // 6. Check whether user is already authenticated
            if (userEmail != null &&
                    role != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // 7. Create Spring Security authority
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(role);

                // 8. Create Authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                List.of(authority)
                        );

                // 9. Add request details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 10. Store authentication in SecurityContext
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }

        } catch (Exception e) {

            // Invalid/expired JWT
            SecurityContextHolder.clearContext();
        }

        // 11. Continue filter chain
        filterChain.doFilter(request, response);
    }
}