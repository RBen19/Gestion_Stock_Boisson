package org.beni.gestionboisson.auth.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.beni.gestionboisson.auth.exceptions.*;
import org.beni.gestionboisson.shared.response.ApiResponse;
import jakarta.ws.rs.core.Response;
import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.dto.RefreshTokenRequestDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
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
            return Response.ok(ApiResponse.success(authService.login(authRequestDTO))).build();
        } catch (PasswordChangeRequiredException e) {
            logger.warn("Password change required for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.FORBIDDEN).entity(ApiResponse.error(e.getMessage(), Response.Status.FORBIDDEN.getStatusCode())).build();
        } catch (InvalidCredentialsException e) {
            logger.error("Login failed for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode())).build();
        } catch (UserNotActiveException e) {
            logger.error("Login failed for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during login for user {}: {}", authRequestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
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
                return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error("Username already exists", Response.Status.CONFLICT.getStatusCode())).build();
            }
            if (utilisateurService.checkEmailExists(utilisateurDTO.getEmail())) {
                logger.warn("Registration failed: Email {} already exists.", utilisateurDTO.getEmail());
                return Response.status(Response.Status.CONFLICT).entity(ApiResponse.error("Email already exists", Response.Status.CONFLICT.getStatusCode())).build();
            }
            return Response.ok(ApiResponse.success(utilisateurService.createUtilisateur(utilisateurDTO, utilisateurDTO.getRoleCode()))).build();
        } catch (RoleNotFoundException e) {
            logger.error("Registration failed for user {}: {}", utilisateurDTO.getNomUtilisateur(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for user {}: {}", utilisateurDTO.getNomUtilisateur(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during registration for user {}: {}", utilisateurDTO.getNomUtilisateur(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordRequestDTO requestDTO) {
        logger.debug("Received change password request for email: {}", requestDTO.getEmail());
        try {
            return Response.ok(ApiResponse.success(authService.changePassword(requestDTO))).build();
        } catch (UserNotFoundException | InvalidOldPasswordException e) {
            logger.error("Password change failed for email {}: {}", requestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ApiResponse.error(e.getMessage(), Response.Status.BAD_REQUEST.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during password change for email {}: {}", requestDTO.getEmail(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshAccessToken(RefreshTokenRequestDTO requestDTO) {
        logger.debug("Received refresh token request.");
        try {
            return Response.ok(ApiResponse.success(authService.refreshAccessToken(requestDTO.getRefreshToken()))).build();
        } catch (UserNotFoundException | InvalidRefreshTokenException e) {
            logger.error("Refresh token failed: {}", e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode())).build();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during refresh token process: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ApiResponse.error("An unexpected error occurred", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())).build();
        }
    }
}
