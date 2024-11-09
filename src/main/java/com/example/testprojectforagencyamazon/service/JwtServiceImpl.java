package com.example.testprojectforagencyamazon.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableCaching
public class JwtServiceImpl implements JwtService {

    private static final String ROLES_CLAIM = "roles";
    private static final String TYPE_CLAIM = "type";
    private static final String REFRESH_TYPE = "refresh";

    @Value("${jwt.signingKey}")
    private String signingKey;

    private Key key;

    @PostConstruct
    public void setKey() {
        if (signingKey == null || signingKey.isEmpty()) {
            throw new IllegalArgumentException("JWT signing key must be specified");
        }
        key = Keys.hmacShaKeyFor(signingKey.getBytes());
    }

    @Override
    public boolean isTokenExpired(String token) {
        return resolveClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public Duration extractDuration(String token) {
        Date expirationDate = resolveClaim(token, Claims::getExpiration);
        return Duration.between(Instant.now(), expirationDate.toInstant());
    }

    @Override
    public String extractUsername(String token) {
        return resolveClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return generateToken(Map.of(ROLES_CLAIM, roles), userDetails, Duration.ofHours(2));
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails, Duration duration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isRefreshType(String token) {
        return resolveClaim(token, claims -> Objects.equals(claims.get(TYPE_CLAIM, String.class), REFRESH_TYPE));
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(Map.of(TYPE_CLAIM, REFRESH_TYPE), userDetails, Duration.ofDays(2));
    }

    private <T> T resolveClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                 | SignatureException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}


