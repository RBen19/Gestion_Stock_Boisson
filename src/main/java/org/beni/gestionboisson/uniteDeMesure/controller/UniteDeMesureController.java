package org.beni.gestionboisson.uniteDeMesure.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.service.UniteDeMesureService;

@Path("/unites-de-mesure")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UniteDeMesureController {

    @Inject
    private UniteDeMesureService uniteDeMesureService;

    @POST
    public Response createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO) {
        UniteDeMesureDTO createdUniteDeMesure = uniteDeMesureService.createUniteDeMesure(uniteDeMesureDTO);
        return Response.status(Response.Status.CREATED).entity(createdUniteDeMesure).build();
    }

    @GET
    public Response getAllUniteDeMesures() {
        return Response.ok(uniteDeMesureService.getAllUniteDeMesures()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUniteDeMesureById(@PathParam("id") Long id) {
        return Response.ok(uniteDeMesureService.getUniteDeMesureById(id)).build();
    }

    @GET
    @Path("/code/{code}")
    public Response getUniteDeMesureByCode(@PathParam("code") String code) {
        return Response.ok(uniteDeMesureService.getUniteDeMesureByCode(code)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUniteDeMesure(@PathParam("id") Long id) {
        uniteDeMesureService.deleteUniteDeMesure(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/seed")
    public Response seedUniteDeMesure() {
        uniteDeMesureService.seedUniteDeMesure();
        return Response.ok("data seed").build();
    }
}
