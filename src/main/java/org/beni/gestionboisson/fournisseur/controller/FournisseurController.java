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

@Path("/fournisseurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class FournisseurController {

    @Inject
    private FournisseurService fournisseurService;

    @Inject
    private FournisseurSeeder fournisseurSeeder;

    @POST
    public Response createFournisseur(CreateFournisseurDTO createFournisseurDTO) {
        try {
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(fournisseurService.createFournisseur(createFournisseurDTO))).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }

    @GET
    public Response getAllFournisseurs() {
        return Response.ok(ApiResponse.success(fournisseurService.getAllFournisseurs())).build();
    }

    @GET
    @Path("/{code}")
    public Response getFournisseurByCode(@PathParam("code") String code) {
        try {
            FournisseurDTO fournisseurDTO = fournisseurService.getFournisseurByCode(code);
            if(fournisseurDTO==null){
              return   Response.status(Response.Status.NOT_FOUND).entity("not found founisseur with this code").build();
            }
            return Response.ok(ApiResponse.success(fournisseurDTO)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{code}")
    public Response updateFournisseur(@PathParam("code") String code, UpdateFournisseurDTO updateFournisseurDTO) {
        try {
            return Response.ok(ApiResponse.success(fournisseurService.updateFournisseur(code, updateFournisseurDTO))).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }

    @PATCH
    @Path("/status/{code}")
    public Response changeStatusByCode(@PathParam("code") String code, ChangeStatusDTO changeStatusDTO) {
        try {
            fournisseurService.changeStatusByCode(code, changeStatusDTO.isStatus());
            return Response.ok(ApiResponse.success("Status changed successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }

    @GET
    @Path("/seed")
    public Response seedFournisseurs() {
        fournisseurSeeder.seedFournisseurs();
        return Response.ok(ApiResponse.success("Fournisseurs seeded successfully")).build();
    }
}
