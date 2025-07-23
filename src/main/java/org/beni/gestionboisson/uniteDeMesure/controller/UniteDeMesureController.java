package org.beni.gestionboisson.uniteDeMesure.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.exceptions.DuplicateUniteDeMesureException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.InvalidUniteDeMesureRequestException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.UniteDeMesureNotFoundException;
import org.beni.gestionboisson.uniteDeMesure.service.UniteDeMesureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

@Path("/unites-de-mesure")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UniteDeMesureController {

    private static final Logger logger = LoggerFactory.getLogger(UniteDeMesureController.class);

    @Inject
    private UniteDeMesureService uniteDeMesureService;
    @POST
    @Path("/seed")
    public Response seedUniteDeMesure() {
        try {
            logger.info("Received request to seed unites de mesure.");
            uniteDeMesureService.seedUniteDeMesure();
            logger.info("Unites de mesure successfully seeded.");
            return Response.status(Response.Status.OK).entity(ApiResponse.success("Unites de mesure seed successful")).build();

        } catch (Exception e) {
            logger.error("An error occurred while seeding unites de mesure: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while seeding the unites de mesure", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @GET
    public Response getAllUnitesDeMesure() {
        logger.info("Received request to get all units of measure.");
        List<UniteDeMesureDTO> unites = uniteDeMesureService.getAllUnitesDeMesure();
        logger.info("Returning {} units of measure.", unites.size());
        return Response.ok(ApiResponse.success(unites)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUniteDeMesureById(@PathParam("id") Long id) {
        logger.info("Received request to get unit of measure by ID: {}", id);
        try {
            UniteDeMesureDTO unite = uniteDeMesureService.getUniteDeMesureById(id);
            logger.info("Successfully retrieved unit of measure with ID: {}", id);
            return Response.ok(ApiResponse.success(unite)).build();
        } catch (UniteDeMesureNotFoundException e) {
            logger.warn("Unit of measure with ID {} not found: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode()))
                    .build();
        } catch (Exception e) {
            logger.error("Error retrieving unit of measure with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @POST
    public Response createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Received request to create new unit of measure with code: {}", uniteDeMesureDTO.getCode());
        try {
            UniteDeMesureDTO createdUnite = uniteDeMesureService.createUniteDeMesure(uniteDeMesureDTO);
            logger.info("Successfully created unit of measure with ID: {}", createdUnite.getId());
            return Response.created(URI.create("/unitesdemesure/" + createdUnite.getId()))
                    .entity(ApiResponse.success(createdUnite))
                    .build();
        } catch (DuplicateUniteDeMesureException e) {
            logger.warn("Duplicate unit of measure creation attempt: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode()))
                    .build();
        } catch (InvalidUniteDeMesureRequestException e) {
            logger.warn("Invalid unit of measure creation request: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        } catch (Exception e) {
            logger.error("Error creating unit of measure: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateUniteDeMesure(@PathParam("id") Long id, UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Received request to update unit of measure with ID: {}", id);
        try {
            UniteDeMesureDTO updatedUnite = uniteDeMesureService.updateUniteDeMesure(id, uniteDeMesureDTO);
            logger.info("Successfully updated unit of measure with ID: {}", id);
            return Response.ok(ApiResponse.success(updatedUnite)).build();
        } catch (UniteDeMesureNotFoundException e) {
            logger.warn("Unit of measure with ID {} not found for update: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode()))
                    .build();
        } catch (DuplicateUniteDeMesureException e) {
            logger.warn("Duplicate unit of measure update attempt: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode()))
                    .build();
        } catch (InvalidUniteDeMesureRequestException e) {
            logger.warn("Invalid unit of measure update request: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode()))
                    .build();
        } catch (Exception e) {
            logger.error("Error updating unit of measure with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUniteDeMesure(@PathParam("id") Long id) {
        logger.info("Received request to delete unit of measure with ID: {}", id);
        try {
            uniteDeMesureService.deleteUniteDeMesure(id);
            logger.info("Successfully deleted unit of measure with ID: {}", id);
            return Response.noContent().build();
        } catch (UniteDeMesureNotFoundException e) {
            logger.warn("Unit of measure with ID {} not found for deletion: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode()))
                    .build();
        } catch (Exception e) {
            logger.error("Error deleting unit of measure with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Internal server error", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }
}