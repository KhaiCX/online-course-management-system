package com.authservice.config;

import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private final String application;

    private final SecretKey secretKey;

    public JwtTokenProvider(
            @Value("${spring.application.name}") String application, @Value("${secret}") String secretKey) {
        this.application = application;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String userId, Collection<? extends GrantedAuthority> role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 60 * 60 * 1000);
        return Jwts.builder()
                .subject(userId)
                .issuer(application)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

}
