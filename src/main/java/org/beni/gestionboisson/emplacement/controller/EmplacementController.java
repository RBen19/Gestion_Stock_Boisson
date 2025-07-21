package org.beni.gestionboisson.emplacement.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.service.EmplacementService;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.auth.security.Secured;

import java.util.List;

@Path("/emplacements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmplacementController {

    @Inject
    EmplacementService emplacementService;

    @POST
    @Path("/seed")
    @Secured
    public Response seedEmplacements() {
        emplacementService.seedEmplacements();
        return Response.ok("Emplacements seeded successfully").build();
    }

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmplacement(EmplacementDTO dto) {
        EmplacementDTO createdEmplacement = emplacementService.createEmplacement(dto);
        return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdEmplacement)).build();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllEmplacements() {
        List<EmplacementDTO> emplacements = emplacementService.getAllEmplacements();
      //  return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();

        return Response.ok(ApiResponse.success(emplacements)).build();
    }

    @GET
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEmplacementByCode(@PathParam("code") String code) {
        EmplacementDTO emplacement = emplacementService.getEmplacementByCode(code);
        if (emplacement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("not found emplacent with this code",404)).build();
        }
        return Response.status(Response.Status.OK).entity(ApiResponse.success(emplacement)).build();
    }

    @PUT
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEmplacement(@PathParam("code") String code, EmplacementDTO dto) {
        try{
            EmplacementDTO updatedEmplacement = emplacementService.updateEmplacement(code, dto);
            return Response.status(Response.Status.OK).entity(ApiResponse.success(updatedEmplacement)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();

        }

    }

    @DELETE
    @Path("/{code}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteEmplacement(@PathParam("code") String code) {
        try {

        } catch (Exception e) {

        }
        emplacementService.deleteEmplacement(code);
        return Response.noContent().entity( ApiResponse.success("Emplacement deleted successfully") ).build();
    }
}
