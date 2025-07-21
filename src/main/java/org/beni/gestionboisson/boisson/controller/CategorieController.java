package org.beni.gestionboisson.boisson.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
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
    @Path("/seed")
    public Response seedCategories() {
        categorieService.seedCategories();
        return Response.ok(ApiResponse.success("Categories seeded successfully")).build();
    }
}
