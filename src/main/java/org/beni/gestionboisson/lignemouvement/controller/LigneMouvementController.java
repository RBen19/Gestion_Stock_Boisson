package org.beni.gestionboisson.lignemouvement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;
import org.beni.gestionboisson.lignemouvement.service.LigneMouvementService;
import org.beni.gestionboisson.shared.custom.EntityNotFoundException;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/ligne-mouvements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LigneMouvementController {

    private static final Logger logger = LoggerFactory.getLogger(LigneMouvementController.class);

    @Inject
    LigneMouvementService ligneMouvementService;

    @POST
    @Secured
    public Response createLigneMouvement(LigneMouvementCreateDTO ligneMouvementCreateDTO) {
        logger.info("Received request to create ligneMouvement for mouvement ID: {}", ligneMouvementCreateDTO.getMouvementId());
        try {
            LigneMouvementDTO createdLigneMouvement = ligneMouvementService.createLigneMouvement(ligneMouvementCreateDTO);
            logger.info("LigneMouvement created successfully with ID: {}", createdLigneMouvement.getId());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdLigneMouvement)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("Error creating ligneMouvement: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.notFound(e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Unexpected error creating ligneMouvement: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error( "Internal server error", 500)).build();
        }
    }

    @GET
    @Secured
    @Path("/{id}")
    public Response getLigneMouvementById(@PathParam("id") Long id) {
        logger.info("Received request to get ligneMouvement by ID: {}", id);
        try {
            LigneMouvementDTO ligneMouvementDTO = ligneMouvementService.getLigneMouvementById(id);
            logger.info("LigneMouvement found for ID: {}", id);
            return Response.ok(ApiResponse.success(ligneMouvementDTO)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("Error retrieving ligneMouvement: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.notFound(e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ligneMouvement: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("Internal server error", 500)).build();
        }
    }

    @GET
    @Secured
    public Response getAllLigneMouvements() {
        logger.info("Received request to get all ligneMouvements");
        try {
            List<LigneMouvementDTO> ligneMouvements = ligneMouvementService.getAllLigneMouvements();
            logger.info("Retrieved {} ligneMouvements", ligneMouvements.size());
            return Response.ok(ApiResponse.success(ligneMouvements)).build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving all ligneMouvements: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error( "Internal server error", 500)).build();
        }
    }

    @GET
    @Secured
    @Path("/mouvement/{mouvementId}")
    public Response getLigneMouvementsByMouvementId(@PathParam("mouvementId") Long mouvementId) {
        logger.info("Received request to get ligneMouvements for mouvement ID: {}", mouvementId);
        try {
            List<LigneMouvementDTO> ligneMouvements = ligneMouvementService.getLigneMouvementsByMouvementId(mouvementId);
            logger.info("Retrieved {} ligneMouvements for mouvement ID: {}", ligneMouvements.size(), mouvementId);
            return Response.ok(ApiResponse.success(ligneMouvements)).build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving ligneMouvements for mouvement ID {}: {}", mouvementId, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.errorConflict("Internal server error")).build();
        }
    }
}
