package org.beni.gestionboisson.boisson.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.security.Secured;
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
            return Response.status(Response.Status.CREATED).entity(createdBoisson).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllBoissons() {
        return Response.ok(boissonService.getAllBoissons()).build();
    }

    @GET
    @Path("/{id}")
    public Response getBoissonById(@PathParam("id") Long id) {
        BoissonDTO boissonDTO = boissonService.getBoissonById(id);
        if (boissonDTO != null) {
            return Response.ok(boissonDTO).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
