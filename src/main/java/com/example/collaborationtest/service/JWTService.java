package com.example.collaborationtest.service;

import com.example.collaborationtest.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${app.jwt.secret}")
    private String secret;


    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private String secretKey;

    public JWTService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("hmacSHA256");
            SecretKey key = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        }catch (Exception e) {
            System.out.println("Error creating a secret key");
        }
    }

    public String generateToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs*3))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] keyBytes = decoder.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
