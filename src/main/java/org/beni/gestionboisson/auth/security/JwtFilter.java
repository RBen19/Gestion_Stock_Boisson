package org.beni.gestionboisson.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class JwtFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Inject
    private JwtUtil jwtUtil;

    @Inject
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("JWT Filter: No Authorization header or does not start with Bearer.");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Authorization header must be provided\"}").build());
            return;
        }

        String accessToken = authHeader.substring("Bearer".length()).trim();

        try {
            Claims claims = jwtUtil.validateAccessToken(accessToken);
            TokenContext.setClaims(claims);
            logger.info("JWT Filter: Access token validated successfully.");
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Filter: Access token expired. Attempting to refresh.");
            String refreshToken = requestContext.getHeaderString("X-Refresh-Token");

            if (refreshToken == null || refreshToken.isEmpty()) {
                logger.warn("JWT Filter: Refresh token not provided for expired access token.");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Access token expired, refresh token required\"}").build());
                return;
            }

            try {
                AuthResponseDTO newTokens = authService.refreshAccessToken(refreshToken);
                Claims refreshClaims = jwtUtil.validateAccessToken(newTokens.getAccessToken());
                TokenContext.setClaims(refreshClaims);
                // Update the Authorization header with the new access token for the current request
                requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, "Bearer " + newTokens.getAccessToken());
                logger.info("JWT Filter: Access token refreshed successfully. New access token set in request header.");
            } catch (Exception refreshException) {
                logger.error("JWT Filter: Failed to refresh access token: {}", refreshException.getMessage());
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid or expired refresh token\"}").build());
            }
        } catch (Exception e) {
            logger.error("JWT Filter: Invalid access token: {}", e.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Invalid access token\"}").build());
        }
    }
}
