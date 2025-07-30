package org.beni.gestionboisson.shared.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.shared.database.DatabaseSeederService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/admin/seed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeedController {

    private static final Logger logger = LoggerFactory.getLogger(SeedController.class);

    @Inject
    private DatabaseSeederService databaseSeederService;

    @POST
    @Secured
    @Path("/run")
    public Response runSeeders() {
        logger.info("Demande manuelle d'exécution des seeders");
        
        try {
            databaseSeederService.runAllSeeders();
            return Response.ok(ApiResponse.success("Seeders exécutés avec succès")).build();
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution manuelle des seeders: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Erreur lors de l'exécution des seeders: " + e.getMessage(), 500))
                    .build();
        }
    }

    @POST
    @Secured
    @Path("/reseed")
    public Response reseedAll() {
        logger.info("Demande de réexécution de tous les seeders");
        
        try {
            databaseSeederService.reseedAll();
            return Response.ok(ApiResponse.success("Tous les seeders ont été réexécutés avec succès")).build();
        } catch (Exception e) {
            logger.error("Erreur lors de la réexécution des seeders: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Erreur lors de la réexécution des seeders: " + e.getMessage(), 500))
                    .build();
        }
    }

    @GET
    @Secured
    @Path("/status")
    public Response getSeedingStatus() {
        return Response.ok(ApiResponse.success("Service de seeding opérationnel")).build();
    }
}