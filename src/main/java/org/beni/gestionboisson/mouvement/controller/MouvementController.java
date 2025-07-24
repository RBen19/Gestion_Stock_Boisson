package org.beni.gestionboisson.mouvement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;
import org.beni.gestionboisson.mouvement.service.MouvementService;
import org.beni.gestionboisson.shared.custom.EntityNotFoundException;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/mouvements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MouvementController {

    private static final Logger logger = LoggerFactory.getLogger(MouvementController.class);

    @Inject
    MouvementService mouvementService;

    @POST
    @Secured
    public Response createMouvement(MouvementCreateDTO mouvementCreateDTO) {
        logger.info("Received request to create mouvement: {}", mouvementCreateDTO.getTypeMouvementCode());
        try {
            MouvementDTO createdMouvement = mouvementService.createMouvement(mouvementCreateDTO);
            logger.info("Mouvement created successfully with ID: {}", createdMouvement.getId());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdMouvement)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("Error creating mouvement: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.notFound(e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Unexpected error creating mouvement: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("Internal server error",500)).build();
        }
    }

    @GET
    @Secured
    @Path("/{id}")
    public Response getMouvementById(@PathParam("id") Long id) {
        logger.info("Received request to get mouvement by ID: {}", id);
        try {
            MouvementDTO mouvementDTO = mouvementService.getMouvementById(id);
            logger.info("Mouvement found for ID: {}", id);
            return Response.ok(ApiResponse.success(mouvementDTO)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("Error retrieving mouvement: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.notFound(e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving mouvement: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error( "Internal server error", 500)).build();
        }
    }

    @GET
    @Secured
    public Response getAllMouvements() {
        logger.info("Received request to get all mouvements");
        try {
            List<MouvementDTO> mouvements = mouvementService.getAllMouvements();
            logger.info("Retrieved {} mouvements", mouvements.size());
            return Response.ok(ApiResponse.success(mouvements)).build();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving all mouvements: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error( "Internal server error", 500)).build();
        }
    }
}
