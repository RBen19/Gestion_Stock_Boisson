package org.beni.gestionboisson.fournisseur.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.fournisseur.dto.ChangeStatusDTO;
import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.UpdateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.database.seeders.FournisseurSeeder;
import org.beni.gestionboisson.fournisseur.service.FournisseurService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.fournisseur.exceptions.FournisseurNotFoundException;

@Path("/fournisseurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class FournisseurController {

    private static final Logger logger = LoggerFactory.getLogger(FournisseurController.class);

    @Inject
    private FournisseurService fournisseurService;

    @Inject
    private FournisseurSeeder fournisseurSeeder;

    @POST
    public Response createFournisseur(CreateFournisseurDTO createFournisseurDTO) {
        logger.info("Received request to create fournisseur: {}", createFournisseurDTO.getNom());
        try {
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(fournisseurService.createFournisseur(createFournisseurDTO))).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating fournisseur: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    public Response getAllFournisseurs() {
        logger.info("Received request to get all fournisseurs.");
        try {
            return Response.ok(ApiResponse.success(fournisseurService.getAllFournisseurs())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all fournisseurs: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/{code}")
    public Response getFournisseurByCode(@PathParam("code") String code) {
        logger.info("Received request to get fournisseur by code: {}", code);
        try {
            FournisseurDTO fournisseurDTO = fournisseurService.getFournisseurByCode(code);
            logger.info("Fournisseur found by code: {}", code);
            return Response.ok(ApiResponse.success(fournisseurDTO)).build();
        } catch (FournisseurNotFoundException e) {
            logger.warn("Fournisseur with code {} not found: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving fournisseur by code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{code}")
    public Response updateFournisseur(@PathParam("code") String code, UpdateFournisseurDTO updateFournisseurDTO) {
        logger.info("Received request to update fournisseur with code {}: {}", code, updateFournisseurDTO.getNom());
        try {
            return Response.ok(ApiResponse.success(fournisseurService.updateFournisseur(code, updateFournisseurDTO))).build();
        } catch (FournisseurNotFoundException e) {
            logger.warn("Fournisseur with code {} not found for update: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating fournisseur with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PATCH
    @Path("/status/{code}")
    public Response changeStatusByCode(@PathParam("code") String code, ChangeStatusDTO changeStatusDTO) {
        logger.info("Received request to change status for fournisseur with code {}: {}", code, changeStatusDTO.isStatus());
        try {
            fournisseurService.changeStatusByCode(code, changeStatusDTO.isStatus());
            logger.info("Status changed successfully for fournisseur with code {}.", code);
            return Response.ok(ApiResponse.success("Status changed successfully")).build();
        } catch (FournisseurNotFoundException e) {
            logger.warn("Fournisseur with code {} not found for status change: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while changing status for fournisseur with code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/seed")
    public Response seedFournisseurs() {
        logger.info("Received request to seed fournisseurs.");
        try {
            fournisseurSeeder.seedFournisseurs();
            logger.info("Fournisseurs seeded successfully.");
            return Response.ok(ApiResponse.success("Fournisseurs seeded successfully")).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while seeding fournisseurs: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }
}
