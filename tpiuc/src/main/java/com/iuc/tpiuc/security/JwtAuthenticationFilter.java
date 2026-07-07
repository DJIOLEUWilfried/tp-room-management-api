package com.iuc.tpiuc.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Vérifier Bearer Token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraire le JWT
        jwt = authHeader.substring(7);
        log.debug("JWT extrait: {}", jwt.substring(0, Math.min(20, jwt.length())) + "...");

        // Extraire email
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction du JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Vérifier authentification
<<<<<<< HEAD
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (userEmail != null && jwtService.isTokenBlacklisted(jwt)) {
            log.warn("JWT rejeté car invalidé : {}", userEmail);
            filterChain.doFilter(request, response);
            return;
        }


=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Vérifier validité token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentification JWT réussie pour: {}", userEmail);
                } else {
                    log.warn("JWT invalide pour: {}", userEmail);
                }
            } catch (Exception e) {
                log.error("Erreur lors de la validation JWT: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

}