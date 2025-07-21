package org.beni.gestionboisson.auth.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.shared.response.ApiResponse;
import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.security.Secured;
import org.beni.gestionboisson.auth.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secured
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Inject
    private RoleService roleService;

    @GET
    public Response getAllRoles() {
        logger.info("Getting all roles");
        return Response.ok(ApiResponse.success(roleService.getAllRoles())).build();
    }

    @GET
    @Path("/{id}")
    public Response getRoleById(@PathParam("id") Long id) {
        logger.info("Getting role by id: {}", id);
        RoleDTO roleDTO = roleService.getRoleById(id);
        if (roleDTO != null) {
            return Response.ok(ApiResponse.success(roleDTO)).build();
        } else {
            logger.warn("Role with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error("Role not found", Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @POST
    public Response createRole(RoleDTO roleDTO) {
        logger.info("Creating new role: {}", roleDTO);
        RoleDTO createdRole = roleService.createRole(roleDTO);
        logger.info("Role created: {}", createdRole);
        return Response.status(Response.Status.CREATED).entity(ApiResponse.success(createdRole)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") Long id, RoleDTO roleDTO) {
        logger.info("Updating role with id {}: {}", id, roleDTO);
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
            logger.info("Role updated: {}", updatedRole);
            return Response.ok(ApiResponse.success(updatedRole)).build();
        } catch (RuntimeException e) {
            logger.error("Error updating role with id {}: {}", id, e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ApiResponse.error(e.getMessage(), Response.Status.NOT_FOUND.getStatusCode())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRole(@PathParam("id") Long id) {
        logger.info("Deleting role with id: {}", id);
        roleService.deleteRole(id);
        logger.info("Role with id {} deleted", id);
        return Response.noContent().build();
    }
}
