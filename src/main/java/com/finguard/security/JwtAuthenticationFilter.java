package com.finguard.security;

import com.finguard.model.User;
import com.finguard.repository.UserRepository;
import com.finguard.service.JwtService;
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
import java.util.List;

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

        String authHeader = request.getHeader("Authorization");

        System.out.println(
                "REQUEST: " + request.getMethod() + " " + request.getRequestURI()
        );

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("JWT: NO TOKEN");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            String email = jwtService.extractEmail(token);

            System.out.println("JWT EMAIL: " + email);

            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user != null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(
                                        new org.springframework.security.core.authority
                                                .SimpleGrantedAuthority(
                                                "ROLE_" + user.getRole().name()
                                        )
                                )
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "JWT AUTHENTICATED: " + user.getEmail()
                );

            } else {

                System.out.println("JWT: USER NOT FOUND");
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT ERROR: " + e.getClass().getSimpleName()
                            + " - " + e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}