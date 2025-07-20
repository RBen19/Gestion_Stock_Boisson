package org.beni.gestionboisson.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;

@ApplicationScoped
public class JwtUtil {

    private final String secret = "07ea51ffc27166f55af17a748438b99ac35310a60c5d10545b4e4dceb783cf41"; // Access token secret
    private final long expiration = 3600000; // 1 hour for access token

    private final String refreshSecret = "505e261dc5647ab45ee293af753ddef9ad510acdaa59be3bf875aca1d6dc9c24"; // Refresh token secret
    private final long refreshExpiration = 604800000; // 7 days for refresh token

    public String generateAccessToken(String username, String role, String email) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, refreshSecret)
                .compact();
    }

    public Claims validateAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims validateRefreshToken(String token) {
        return Jwts.parser()
                .setSigningKey(refreshSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
