package org.beni.gestionboisson.type_emplacement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;
import org.beni.gestionboisson.type_emplacement.service.TypeEmplacementService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.auth.security.Secured;

import java.util.List;

@Path("/type-emplacements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TypeEmplacementController {

    @Inject
    TypeEmplacementService typeEmplacementService;

    @POST
    @Path("/seed")
    @Secured
    public Response seedTypeEmplacements() {
        typeEmplacementService.seedTypeEmplacements();
        return Response.ok("TypeEmplacements seeded successfully").build();
    }

    @POST
    @Secured
    public Response createTypeEmplacement(TypeEmplacementDTO dto) {
        TypeEmplacementDTO createdTypeEmplacement = typeEmplacementService.createTypeEmplacement(dto);
        return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdTypeEmplacement)).build();
    }

    @GET
    @Secured
    public Response getAllTypeEmplacements() {
        List<TypeEmplacementDTO> typeEmplacements = typeEmplacementService.getAllTypeEmplacements();
        return Response.ok(ApiResponse.success(typeEmplacements)).build();
    }

    @GET
    @Path("/{code}")
    @Secured
    public Response getTypeEmplacementByCode(@PathParam("code") String code) {
        TypeEmplacementDTO typeEmplacement = typeEmplacementService.getTypeEmplacementByCode(code);
        if (typeEmplacement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("TypeEmplacement not found",404)).build();
        }
        return Response.ok(typeEmplacement).build();
    }

    @PUT
    @Path("/{code}")
    @Secured
    public Response updateTypeEmplacement(@PathParam("code") String code, TypeEmplacementDTO dto) {
        TypeEmplacementDTO updatedTypeEmplacement = typeEmplacementService.updateTypeEmplacement(code, dto);
        return Response.ok(ApiResponse.success(updatedTypeEmplacement) ).build();
    }

    @DELETE
    @Path("/{code}")
    @Secured
    public Response deleteTypeEmplacement(@PathParam("code") String code) {
        typeEmplacementService.deleteTypeEmplacement(code);
        return Response.noContent().entity( ApiResponse.success("TypeEmplacement deleted successfully")).build();
    }
}