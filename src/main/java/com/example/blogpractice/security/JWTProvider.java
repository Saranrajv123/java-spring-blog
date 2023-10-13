package com.example.blogpractice.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTProvider.class);

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${jwtExpirationInMs}")
    private String jwtExpired;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        System.out.println("username "+ username);
//        Date expiryDate = new Date(now.getTime() + jwtExpired);
//                new Date(now.getTime() + jwtExpired);


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
//                .setExpiration(expiryDate)
                .signWith(key())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;

        } catch (MalformedJwtException e) {
            logger.error("jwt validation error: {}", e.getMessage());

        }
        return false;
    }

    public Key key() {
        byte[] bytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUsernameFromJWT(String token) {
        System.out.println("token " + token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJwt(token)
                .getBody();

        return claims.getSubject();
    }


}
