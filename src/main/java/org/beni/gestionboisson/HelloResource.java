package org.beni.gestionboisson;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.shared.database.DatabaseSeederService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {
    private static final Logger logger = LoggerFactory.getLogger(HelloResource.class);

    @Inject
    private DatabaseSeederService databaseSeederService;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }

    @GET
    @Path("/seed")
   // @Produces("text/plain")
    public Response seed() {
        try {
            logger.info("Seeding DB ");
            databaseSeederService.runAllSeeders();
            return Response.ok("Seeding completed").build();
        } catch (Exception e) {
            logger.error("Erreur pendant le seeding : {}", e.getMessage(), e);
            return Response.serverError().entity("Seeding failed: " + e.getMessage()).build();
        }
    }
}