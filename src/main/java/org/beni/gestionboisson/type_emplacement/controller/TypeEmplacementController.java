package org.beni.gestionboisson.type_emplacement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;
import org.beni.gestionboisson.type_emplacement.service.TypeEmplacementService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.auth.security.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.type_emplacement.exceptions.TypeEmplacementNotFoundException;

import java.util.List;

@Path("/type-emplacements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeEmplacementController {

    private static final Logger logger = LoggerFactory.getLogger(TypeEmplacementController.class);

    @Inject
    TypeEmplacementService typeEmplacementService;

    @POST
    @Path("/seed")
    @Secured
    public Response seedTypeEmplacements() {
        logger.info("Received request to seed type emplacements.");
        try {
            typeEmplacementService.seedTypeEmplacements();
            logger.info("TypeEmplacements seeded successfully.");
            return Response.ok(ApiResponse.success("TypeEmplacements seeded successfully")).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while seeding type emplacements: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @POST
    @Secured
    public Response createTypeEmplacement(TypeEmplacementDTO dto) {
        logger.info("Received request to create type emplacement: {}", dto.getLibelle());
        try {
            TypeEmplacementDTO createdTypeEmplacement = typeEmplacementService.createTypeEmplacement(dto);
            logger.info("TypeEmplacement created successfully: {}", createdTypeEmplacement.getLibelle());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdTypeEmplacement)).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating type emplacement: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Secured
    public Response getAllTypeEmplacements() {
        logger.info("Received request to get all type emplacements.");
        try {
            List<TypeEmplacementDTO> typeEmplacements = typeEmplacementService.getAllTypeEmplacements();
            logger.info("Retrieved {} type emplacements.", typeEmplacements.size());
            return Response.ok(ApiResponse.success(typeEmplacements)).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all type emplacements: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/{code}")
    @Secured
    public Response getTypeEmplacementByCode(@PathParam("code") String code) {
        logger.info("Received request to get type emplacement by code: {}", code);
        try {
            TypeEmplacementDTO typeEmplacement = typeEmplacementService.getTypeEmplacementByCode(code);
            logger.info("TypeEmplacement found by code: {}", code);
            return Response.status(Response.Status.OK).entity(ApiResponse.success(typeEmplacement)).build();
        } catch (TypeEmplacementNotFoundException e) {
            logger.warn("TypeEmplacement with code {} not found: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving type emplacement by code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{code}")
    @Secured
    public Response updateTypeEmplacement(@PathParam("code") String code, TypeEmplacementDTO dto) {
        logger.info("Received request to update type emplacement with code {}: {}", code, dto.getLibelle());
        try {
            TypeEmplacementDTO updatedTypeEmplacement = typeEmplacementService.updateTypeEmplacement(code, dto);
            logger.info("TypeEmplacement with code {} updated successfully.", code);
            return Response.ok(ApiResponse.success(updatedTypeEmplacement)).build();
        } catch (TypeEmplacementNotFoundException e) {
            logger.warn("TypeEmplacement with code {} not found for update: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating type emplacement with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{code}")
    @Secured
    public Response deleteTypeEmplacement(@PathParam("code") String code) {
        logger.info("Received request to delete type emplacement with code: {}", code);
        try {
            typeEmplacementService.deleteTypeEmplacement(code);
            logger.info("TypeEmplacement with code {} deleted successfully.", code);
            return Response.noContent().build();
        } catch (TypeEmplacementNotFoundException e) {
            logger.warn("TypeEmplacement with code {} not found for deletion: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting type emplacement with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }
}