package org.beni.gestionboisson.type_mouvement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;
import org.beni.gestionboisson.type_mouvement.service.TypeMouvementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/type-mouvements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeMouvementController {

    private static final Logger logger = LoggerFactory.getLogger(TypeMouvementController.class);

    @Inject
    private TypeMouvementService typeMouvementService;

    @GET
    @Secured
    public Response getAllTypeMouvements() {
        logger.info("Received request to get all type mouvements");
        return Response.ok(ApiResponse.success( typeMouvementService.getAllTypeMouvements())).build();
    }

    @GET
    @Path("/{id}")
    @Secured
    public Response getTypeMouvementById(@PathParam("id") Long id) {
        logger.info("Received request to get type mouvement by ID: {}", id);
        return Response.status(Response.Status.OK).entity(ApiResponse.success(typeMouvementService.getTypeMouvementById(id))).build();
    }

    @POST
    @Secured
    public Response createTypeMouvement(TypeMouvementDTO typeMouvementDTO) {
        logger.info("Received request to create type mouvement with libelle: {}", typeMouvementDTO.getLibelle());
        TypeMouvementDTO createdTypeMouvement = typeMouvementService.createTypeMouvement(typeMouvementDTO);
        return Response.status(Response.Status.CREATED).entity(ApiResponse.success( createdTypeMouvement)).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    public Response updateTypeMouvement(@PathParam("id") Long id, TypeMouvementDTO typeMouvementDTO) {
        logger.info("Received request to update type mouvement with ID: {}", id);
        TypeMouvementDTO updatedTypeMouvement = typeMouvementService.updateTypeMouvement(id, typeMouvementDTO);
        return Response.ok(ApiResponse.success( updatedTypeMouvement)).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    public Response deleteTypeMouvement(@PathParam("id") Long id) {
        logger.info("Received request to delete type mouvement with ID: {}", id);
        typeMouvementService.deleteTypeMouvement(id);
        return Response.noContent().entity(ApiResponse.success(204)).build();
    }

    @POST
    @Path("/seed")
    @Secured
    public Response seedTypeMouvements() {
        logger.info("Received request to seed default type mouvements");
        typeMouvementService.seedTypeMouvements();
        return Response.ok(ApiResponse.success( "Seeded default type mouvements")).build();
    }
}
