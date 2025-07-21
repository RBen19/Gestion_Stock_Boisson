package org.beni.gestionboisson.boisson.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.boisson.service.CategorieService;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class CategorieController {

    @Inject
    private CategorieService categorieService;

    @GET
    public Response getAllCategories() {
        return Response.ok(ApiResponse.success(categorieService.getAllCategories())).build();
    }

    @GET
    @Path("/{id}")
    public Response getCategorieById(@PathParam("id") Long id) {
        CategorieDTO categorieDTO = categorieService.getCategorieById(id);
        if (categorieDTO != null) {
            return Response.ok(ApiResponse.success(categorieDTO)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("Category not found", Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @POST
    public Response createCategorie(CategorieDTO categorieDTO) {
        try {
            CategorieDTO createdCategorie = categorieService.createCategorie(categorieDTO);
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdCategorie)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCategorie(@PathParam("id") Long id, CategorieDTO categorieDTO) {
        try {
            CategorieDTO updatedCategorie = categorieService.updateCategorie(id, categorieDTO);
            return Response.ok(ApiResponse.success(updatedCategorie)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategorie(@PathParam("id") Long id) {
        try {
            categorieService.deleteCategorie(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @GET
    @Path("/code/{code}")
    public Response getCategorieByCode(@PathParam("code") String code) {
        CategorieDTO categorieDTO = categorieService.getCategorieByCode(code);
        if (categorieDTO != null) {
            return Response.ok(ApiResponse.success(categorieDTO)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("Category not found", Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    
}
