package org.beni.gestionboisson.auth.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class HealthController {
    
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "gestion-boisson-backend");
        status.put("timestamp", System.currentTimeMillis());
        
        return Response.ok(status).build();
    }
}