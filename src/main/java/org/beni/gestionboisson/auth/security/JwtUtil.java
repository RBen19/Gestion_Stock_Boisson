package org.beni.gestionboisson.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;

@ApplicationScoped
public class JwtUtil {

    private final String secret = "07ea51ffc27166f55af17a748438b99ac35310a60c5d10545b4e4dceb783cf41"; // Change this to a secure key
    private final long expiration = 86400000; // 24 hours

    public String generateToken(String username, String role,String email) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("email",email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
