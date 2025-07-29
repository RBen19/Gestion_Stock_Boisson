package org.beni.gestionboisson;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RootController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response root() {
        return Response.ok("{\"message\":\"Gestion Boisson API v1\",\"status\":\"running\"}").build();
    }
    
    @HEAD
    public Response rootHead() {
        return Response.ok().build();
    }
}