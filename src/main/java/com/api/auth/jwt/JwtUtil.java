package com.api.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims)Jwts.parserBuilder().setSigningKey(this.SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return Boolean.valueOf(extractExpiration(token).before(new Date()));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 36000000L))
                .signWith(this.SECRET_KEY)

                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return Boolean.valueOf((username.equals(userDetails.getUsername()) && !isTokenExpired(token).booleanValue()));
    }

}
