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

@Path("/boissons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class BoissonController {

    @Inject
    private BoissonService boissonService;


    @POST
    public Response createBoisson(BoissonDTO boissonDTO) {
        try {
            BoissonDTO createdBoisson = boissonService.createBoisson(boissonDTO);
            return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdBoisson)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        }
    }


    @GET
    @Path("/seed")
    public Response seedBoissons() {
      this.boissonService.seedBoissons();
        return Response.status(Response.Status.OK).entity(ApiResponse.success("boisson seed ")).build();

    }

    @GET
    public Response getAllBoissons() {
        return Response.ok(ApiResponse.success(boissonService.getAllBoissons())).build();
    }
    @GET
    @Path("/code-boisson/{codeBoisson}")
    public Response getBoissonByCode(@PathParam("codeBoisson")String codeBoisson){
        try {
            BoissonDTO boissonDTO = boissonService.getBoissonByCode(codeBoisson);
            return  Response.status(Response.Status.OK).entity(ApiResponse.success(boissonDTO)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred while retrieving the boisson", 500))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBoissonById(@PathParam("id") Long id) {
       try {
           BoissonDTO boissonDTO = boissonService.getBoissonById(id);
           if (boissonDTO != null) {
               return Response.ok(ApiResponse.success(boissonDTO)).build();
           } else {
               return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("Boisson not found", Response.Status.NOT_FOUND.getStatusCode())).build();
           }
       } catch (Exception e) {
           return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                   .entity(ApiResponse.error("An error occurred while retrieving the boisson", 500))
                   .build();
       }
    }
}
