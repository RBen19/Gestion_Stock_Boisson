package org.beni.gestionboisson.boisson.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.boisson.service.CategorieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.boisson.exceptions.CategoryNotFoundException;
import org.beni.gestionboisson.boisson.exceptions.InvalidBoissonDataException;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class CategorieController {

    private static final Logger logger = LoggerFactory.getLogger(CategorieController.class);

    @Inject
    private CategorieService categorieService;

    @GET
    public Response getAllCategories() {
        logger.info("Received request to get all categories.");
        try {
            return Response.ok(ApiResponse.success(categorieService.getAllCategories())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all categories: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCategorieById(@PathParam("id") Long id) {
        logger.info("Received request to get category by ID: {}", id);
        try {
            CategorieDTO categorieDTO = categorieService.getCategorieById(id);
            logger.info("Category found by ID: {}", id);
            return Response.ok(ApiResponse.success(categorieDTO)).build();
        } catch (CategoryNotFoundException e) {
            logger.warn("Category with ID {} not found: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving category by ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @POST
    public Response createCategorie(CategorieDTO categorieDTO) {
        logger.info("Received request to create category: {}", categorieDTO.getNom());
        try {
            CategorieDTO createdCategorie = categorieService.createCategorie(categorieDTO);
            logger.info("Category created successfully: {}", createdCategorie.getNom());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdCategorie)).build();
        } catch (InvalidBoissonDataException e) {
            logger.error("Invalid category data: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating category: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCategorie(@PathParam("id") Long id, CategorieDTO categorieDTO) {
        logger.info("Received request to update category with ID {}: {}", id, categorieDTO.getNom());
        try {
            CategorieDTO updatedCategorie = categorieService.updateCategorie(id, categorieDTO);
            logger.info("Category with ID {} updated successfully.", id);
            return Response.ok(ApiResponse.success(updatedCategorie)).build();
        } catch (CategoryNotFoundException e) {
            logger.warn("Category with ID {} not found for update: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (InvalidBoissonDataException e) {
            logger.error("Invalid category data for update: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating category with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategorie(@PathParam("id") Long id) {
        logger.info("Received request to delete category with ID: {}", id);
        try {
            categorieService.deleteCategorie(id);
            logger.info("Category with ID {} deleted successfully.", id);
            return Response.noContent().build();
        } catch (CategoryNotFoundException e) {
            logger.warn("Category with ID {} not found for deletion: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting category with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @GET
    @Path("/code/{code}")
    public Response getCategorieByCode(@PathParam("code") String code) {
        logger.info("Received request to get category by code: {}", code);
        try {
            CategorieDTO categorieDTO = categorieService.getCategorieByCode(code);
            logger.info("Category found by code: {}", code);
            return Response.ok(ApiResponse.success(categorieDTO)).build();
        } catch (CategoryNotFoundException e) {
            logger.warn("Category with code {} not found: {}", code, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving category by code {}: {}", code, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }
}
