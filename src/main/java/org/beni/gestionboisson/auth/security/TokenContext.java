package org.beni.gestionboisson.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;

@ApplicationScoped
public class TokenContext {
    private static final ThreadLocal<Claims> claimsThreadLocal = new ThreadLocal<>();

    public static void setClaims(Claims claims) {
        claimsThreadLocal.set(claims);
    }

    public static Claims getClaims() {
        return claimsThreadLocal.get();
    }

    public static void clear() {
        claimsThreadLocal.remove();
    }

    public static String getUsername() {
        return getClaims() != null ? getClaims().getSubject() : null;
    }

    public static String getEmail() {
        return getClaims() != null ? getClaims().get("email", String.class) : null;
    }

    public static String getRole() {
        return getClaims() != null ? getClaims().get("role", String.class) : null;
    }
}
