
package com.scaler.bookMyShow.config;

import com.scaler.bookMyShow.Repository.UserRepository;
import com.scaler.bookMyShow.model.User;
import com.scaler.bookMyShow.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        System.out.println("JWT filter reached: " + request.getRequestURI());
        System.out.println("Authorization header present: "
                + (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")));

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            String email = jwtService.extractEmail(token);
            System.out.println("JWT email extracted: " + email);

            User user = userRepository.findByEmail(email).orElse(null);
            System.out.println("JWT user found: " + (user != null));

            boolean tokenValid = jwtService.isTokenValid(token, email);
            System.out.println("JWT token valid: " + tokenValid);

            if (user != null && tokenValid
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.emptyList()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("JWT authentication successful.");
            }

        } catch (JwtException | IllegalArgumentException exception) {
            System.out.println("JWT validation failed: "
                    + exception.getClass().getSimpleName()
                    + " - " + exception.getMessage());

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
