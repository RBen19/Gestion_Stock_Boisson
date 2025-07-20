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
import org.beni.gestionboisson.auth.dto.RefreshTokenRequestDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;
import org.beni.gestionboisson.auth.service.AuthService;
import org.beni.gestionboisson.auth.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Inject
    private AuthService authService;

    @Inject
    private UtilisateurService utilisateurService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequestDTO authRequestDTO) {
        logger.debug("Received login request for user: {}", authRequestDTO.getEmail());
        try {
            Response response = Response.ok(authService.login(authRequestDTO)).build();
            logger.info("Login successful for user: {}", authRequestDTO.getEmail());
            return response;
        } catch (PasswordChangeRequiredException e) {
            logger.warn("Password change required for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UtilisateurDTO utilisateurDTO) {
        logger.debug("Received registration request for user: {}", utilisateurDTO.getNomUtilisateur());
        try {
            if (utilisateurService.checkNomUtilisateurExists(utilisateurDTO.getNomUtilisateur())) {
                logger.warn("Registration failed: Username {} already exists.", utilisateurDTO.getNomUtilisateur());
                return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Username already exists\"}").build();
            }
            if (utilisateurService.checkEmailExists(utilisateurDTO.getEmail())) {
                logger.warn("Registration failed: Email {} already exists.", utilisateurDTO.getEmail());
                return Response.status(Response.Status.CONFLICT).entity("{\"error\": \"Email already exists\"}").build();
            }
            Response response = Response.ok(utilisateurService.createUtilisateur(utilisateurDTO, utilisateurDTO.getRoleCode())).build();
            logger.info("User registered successfully: {}", utilisateurDTO.getNomUtilisateur());
            return response;
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for user {}: {}", utilisateurDTO.getNomUtilisateur(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during registration for user {}: {}", utilisateurDTO.getNomUtilisateur(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"An unexpected error occurred\"}").build();
        }
    }

    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordRequestDTO requestDTO) {
        logger.debug("Received change password request for email: {}", requestDTO.getEmail());
        try {
            Response response = Response.ok(authService.changePassword(requestDTO)).build();
            logger.info("Password changed successfully for email: {}", requestDTO.getEmail());
            return response;
        } catch (Exception e) {
            logger.error("Password change failed for email {}: {}", requestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshAccessToken(RefreshTokenRequestDTO requestDTO) {
        logger.debug("Received refresh token request.");
        try {
            Response response = Response.ok(authService.refreshAccessToken(requestDTO.getRefreshToken())).build();
            logger.info("Access token refreshed successfully.");
            return response;
        } catch (Exception e) {
            logger.error("Refresh token failed: {}", e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}
