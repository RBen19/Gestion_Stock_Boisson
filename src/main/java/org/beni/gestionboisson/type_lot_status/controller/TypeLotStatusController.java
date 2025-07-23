package org.beni.gestionboisson.type_lot_status.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;
import org.beni.gestionboisson.type_lot_status.exceptions.DuplicateEntityException;
import org.beni.gestionboisson.type_lot_status.exceptions.EntityNotFoundException;
import org.beni.gestionboisson.type_lot_status.service.TypeLotStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/type-lot-statuses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class TypeLotStatusController {

    private static final Logger logger = LoggerFactory.getLogger(TypeLotStatusController.class);

    @Inject
    private TypeLotStatusService typeLotStatusService;

    @POST
    public Response createTypeLotStatus(TypeLotStatusDTO typeLotStatusDTO) {
        logger.info("Received request to create TypeLotStatus with libelle: {}", typeLotStatusDTO.getLibelle());
        try {
            TypeLotStatusDTO createdTypeLotStatus = typeLotStatusService.createTypeLotStatus(typeLotStatusDTO);
            logger.info("TypeLotStatus created successfully with ID: {}", createdTypeLotStatus.getId());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdTypeLotStatus)).build();
        } catch (DuplicateEntityException e) {
            logger.warn("Creation failed: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating TypeLotStatus: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTypeLotStatus(@PathParam("id") Long id, TypeLotStatusDTO typeLotStatusDTO) {
        logger.info("Received request to update TypeLotStatus with ID: {}", id);
        try {
            TypeLotStatusDTO updatedTypeLotStatus = typeLotStatusService.updateTypeLotStatus(id, typeLotStatusDTO);
            logger.info("TypeLotStatus updated successfully with ID: {}", updatedTypeLotStatus.getId());
            return Response.ok(ApiResponse.success(updatedTypeLotStatus)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("Update failed: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (DuplicateEntityException e) {
            logger.warn("Update failed: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error(e.getMessage(), Response.Status.CONFLICT.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating TypeLotStatus with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTypeLotStatus(@PathParam("id") Long id) {
        logger.info("Received request to delete TypeLotStatus with ID: {}", id);
        try {
            typeLotStatusService.deleteTypeLotStatus(id);
            logger.info("TypeLotStatus deleted successfully with ID: {}", id);
            return Response.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Deletion failed: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting TypeLotStatus with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getTypeLotStatusById(@PathParam("id") Long id) {
        logger.info("Received request to get TypeLotStatus by ID: {}", id);
        try {
            TypeLotStatusDTO typeLotStatusDTO = typeLotStatusService.getTypeLotStatusById(id);
            logger.info("TypeLotStatus found by ID: {}", id);
            return Response.ok(ApiResponse.success(typeLotStatusDTO)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("TypeLotStatus with ID {} not found: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving TypeLotStatus by ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/slug/{slug}")
    public Response getTypeLotStatusBySlug(@PathParam("slug") String slug) {
        logger.info("Received request to get TypeLotStatus by slug: {}", slug);
        try {
            TypeLotStatusDTO typeLotStatusDTO = typeLotStatusService.getTypeLotStatusBySlug(slug);
            logger.info("TypeLotStatus found by slug: {}", slug);
            return Response.ok(ApiResponse.success(typeLotStatusDTO)).build();
        } catch (EntityNotFoundException e) {
            logger.warn("TypeLotStatus with slug {} not found: {}", slug, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving TypeLotStatus by slug {}: {}", slug, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    public Response getAllTypeLotStatuses() {
        logger.info("Received request to get all TypeLotStatuses.");
        try {
            return Response.ok(ApiResponse.success(typeLotStatusService.getAllTypeLotStatuses())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all TypeLotStatuses: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/seed")
    public Response seedTypeLotStatuses() {
        logger.info("Received request to seed TypeLotStatuses.");
        try {
            typeLotStatusService.seedTypeLotStatuses();
            logger.info("TypeLotStatuses seeded successfully.");
            return Response.status(Response.Status.OK).entity(ApiResponse.success("TypeLotStatus seed successful")).build();
        } catch (Exception e) {
            logger.error("An error occurred while seeding TypeLotStatuses: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while seeding TypeLotStatuses", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }
}
