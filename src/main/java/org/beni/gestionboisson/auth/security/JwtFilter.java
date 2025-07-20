package org.beni.gestionboisson.auth.security;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Secured
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.substring("Bearer".length()).trim();

        try {
            jwtUtil.validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
