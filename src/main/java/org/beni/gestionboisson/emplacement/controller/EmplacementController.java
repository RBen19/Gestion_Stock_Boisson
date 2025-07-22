package org.beni.gestionboisson.emplacement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.service.EmplacementService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.auth.security.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.emplacement.exceptions.EmplacementNotFoundException;
import org.beni.gestionboisson.emplacement.exceptions.DuplicateEmplacementCodeException;

import java.util.List;

@Path("/emplacements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmplacementController {

    private static final Logger logger = LoggerFactory.getLogger(EmplacementController.class);

    @Inject
    EmplacementService emplacementService;

    @POST
    @Path("/seed")
    @Secured
    public Response seedEmplacements() {
        logger.info("Received request to seed emplacements.");
        try {
            emplacementService.seedEmplacements();
            logger.info("Emplacements seeded successfully.");
            return Response.ok(ApiResponse.success("Emplacements seeded successfully")).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while seeding emplacements: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmplacement(EmplacementDTO dto) {
        logger.info("Received request to create emplacement: {}", dto.getNom());
        try {
            EmplacementDTO createdEmplacement = emplacementService.createEmplacement(dto);
            logger.info("Emplacement created successfully: {}", createdEmplacement.getCodeEmplacement());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdEmplacement)).build();
        } catch (DuplicateEmplacementCodeException e) {
            logger.warn("Emplacement creation failed: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating emplacement: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllEmplacements() {
        logger.info("Received request to get all emplacements.");
        try {
            List<EmplacementDTO> emplacements = emplacementService.getAllEmplacements();
            logger.info("Retrieved {} emplacements.", emplacements.size());
            return Response.ok(ApiResponse.success(emplacements)).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all emplacements: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEmplacementByCode(@PathParam("code") String code) {
        logger.info("Received request to get emplacement by code: {}", code);
        try {
            EmplacementDTO emplacement = emplacementService.getEmplacementByCode(code);
            logger.info("Emplacement found by code: {}", code);
            return Response.status(Response.Status.OK).entity(ApiResponse.success(emplacement)).build();
        } catch (EmplacementNotFoundException e) {
            logger.warn("Emplacement with code {} not found: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving emplacement by code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmplacement(@PathParam("code") String code, EmplacementDTO dto) {
        logger.info("Received request to update emplacement with code {}: {}", code, dto.getNom());
        try {
            EmplacementDTO updatedEmplacement = emplacementService.updateEmplacement(code, dto);
            logger.info("Emplacement with code {} updated successfully.", code);
            return Response.status(Response.Status.OK).entity(ApiResponse.success(updatedEmplacement)).build();
        } catch (EmplacementNotFoundException e) {
            logger.warn("Emplacement with code {} not found for update: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (DuplicateEmplacementCodeException e) {
            logger.warn("Emplacement update failed: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating emplacement with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEmplacement(@PathParam("code") String code) {
        logger.info("Received request to delete emplacement with code: {}", code);
        try {
            emplacementService.deleteEmplacement(code);
            logger.info("Emplacement with code {} deleted successfully.", code);
            return Response.noContent().build();
        } catch (EmplacementNotFoundException e) {
            logger.warn("Emplacement with code {} not found for deletion: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting emplacement with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }
}
