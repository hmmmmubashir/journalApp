package com.engdigest.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {

    private String secretKey = "w7Kx9mP2vQ8nR4tY6uI1oL3sF5aZ0cD7eG9hJ2kN4pM=";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(username, claims);
    }


    public String createToken(String subject, HashMap<String, Object> claims) {
        return Jwts.builder().claims(claims).subject(subject).header().empty().add("typ", "JWT").and().issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getSigningKey()).compact();
    }

    public String extractUsername(String token){
        return  exctractAllClaims(token).getSubject();
    }

    public Claims exctractAllClaims(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public Date extractExpiration(String token){
        return exctractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token){
        return !isTokenExpired(token);
    }
}
