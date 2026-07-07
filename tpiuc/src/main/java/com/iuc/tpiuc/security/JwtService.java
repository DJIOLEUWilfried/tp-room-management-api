package com.iuc.tpiuc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
<<<<<<< HEAD
import java.util.concurrent.ConcurrentHashMap;
=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

<<<<<<< HEAD
    private final Map<String, Date> invalidatedTokens = new ConcurrentHashMap<>();


=======
>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
    // Génération simple du token
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Génération avec claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.info("Génération JWT pour {}", userDetails.getUsername());

        // Chargons les roles
        extraClaims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraction email
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraction d'un claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Vérifie si token valide
    public boolean isTokenValid(String token, UserDetails userDetails) {
<<<<<<< HEAD
        return !isTokenBlacklisted(token)
                && extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenBlacklisted(String token) {
        removeExpiredInvalidatedTokens();
        return invalidatedTokens.containsKey(token);
    }

    public void invalidateToken(String token) {
        try {
            invalidatedTokens.put(token, extractExpiration(token));
            removeExpiredInvalidatedTokens();
            log.info("JWT invalidé : {}", token.substring(0, Math.min(20, token.length())) + "...");
        } catch (Exception e) {
            log.warn("Impossible d'invalider le token : {}", e.getMessage());
        }
    }

    private void removeExpiredInvalidatedTokens() {
        Date now = new Date();
        invalidatedTokens.entrySet().removeIf(entry -> entry.getValue().before(now));
    }


=======
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

>>>>>>> 4d12fc6494c154745a9d256f6cd6cbbe6797b0d8
    // Vérifie expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Date expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Tous les claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Clé de signature
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}