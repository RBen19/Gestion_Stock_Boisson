package org.beni.gestionboisson.boisson.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.boisson.database.seeders.BoissonSeeder;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.service.BoissonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.boisson.exceptions.BoissonNotFoundException;
import org.beni.gestionboisson.boisson.exceptions.InvalidBoissonDataException;

@Path("/boissons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class BoissonController {

    private static final Logger logger = LoggerFactory.getLogger(BoissonController.class);

    @Inject
    private BoissonService boissonService;


    @POST
    public Response createBoisson(BoissonDTO boissonDTO) {
        logger.info("Received request to create boisson: {}", boissonDTO.getNom());
        try {
            BoissonDTO createdBoisson = boissonService.createBoisson(boissonDTO);
            logger.info("Boisson created successfully: {}", createdBoisson.getNom());
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdBoisson)).build();
        } catch (InvalidBoissonDataException e) {
            logger.error("Invalid boisson data: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating boisson: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }


    @GET
    @Path("/seed")
    public Response seedBoissons() {
        logger.info("Received request to seed boissons.");
        try {
            this.boissonService.seedBoissons();
            logger.info("Boissons seeded successfully.");
            return Response.status(Response.Status.OK).entity(ApiResponse.success("Boisson seed successful")).build();
        } catch (Exception e) {
            logger.error("An error occurred while seeding boissons: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while seeding the boisson", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @GET
    public Response getAllBoissons() {
        logger.info("Received request to get all boissons.");
        try {
            return Response.ok(ApiResponse.success(boissonService.getAllBoissons())).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving all boissons: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while retrieving all boissons", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @GET
    @Path("/code-boisson/{codeBoisson}")
    public Response getBoissonByCode(@PathParam("codeBoisson") String codeBoisson) {
        logger.info("Received request to get boisson by code: {}", codeBoisson);
        try {
            BoissonDTO boissonDTO = boissonService.getBoissonByCode(codeBoisson);
            logger.info("Boisson found by code: {}", codeBoisson);
            return Response.status(Response.Status.OK).entity(ApiResponse.success(boissonDTO)).build();
        } catch (BoissonNotFoundException e) {
            logger.warn("Boisson with code {} not found: {}", codeBoisson, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving boisson by code {}: {}", codeBoisson, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while retrieving the boisson", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBoissonById(@PathParam("id") Long id) {
        logger.info("Received request to get boisson by ID: {}", id);
        try {
            BoissonDTO boissonDTO = boissonService.getBoissonById(id);
            logger.info("Boisson found by ID: {}", id);
            return Response.ok(ApiResponse.success(boissonDTO)).build();
        } catch (BoissonNotFoundException e) {
            logger.warn("Boisson with ID {} not found: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving boisson by ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while retrieving the boisson", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                    .build();
        }
    }
}
