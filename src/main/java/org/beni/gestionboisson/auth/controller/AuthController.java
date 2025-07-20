package org.beni.gestionboisson.auth.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;
import org.beni.gestionboisson.auth.service.AuthService;
import org.beni.gestionboisson.auth.service.UtilisateurService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private AuthService authService;

    @Inject
    private UtilisateurService utilisateurService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequestDTO authRequestDTO) {
        try {
            return Response.ok(authService.login(authRequestDTO)).build();
        } catch (PasswordChangeRequiredException e) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UtilisateurDTO utilisateurDTO) {
        if (utilisateurService.checkNomUtilisateurExists(utilisateurDTO.getNomUtilisateur())) {
            return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Username already exists\"}").build();
        }
        if (utilisateurService.checkEmailExists(utilisateurDTO.getEmail())) {
            return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Email already exists\"}").build();
        }
        try {
            return Response.ok(utilisateurService.createUtilisateur(utilisateurDTO, utilisateurDTO.getRoleCode())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordRequestDTO requestDTO) {
        try {
            return Response.ok(authService.changePassword(requestDTO)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}
