package org.beni.gestionboisson.auth.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.exceptions.*;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;
import org.beni.gestionboisson.auth.security.JwtUtil;
import org.beni.gestionboisson.auth.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) throws PasswordChangeRequiredException {
        logger.info("Attempting login for user: {}", authRequestDTO.getEmail());
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        logger.debug("Hashed password from DB for {}: {}", authRequestDTO.getEmail(), utilisateur.getMotDePasseHache());

        if (!BCrypt.checkpw(authRequestDTO.getPassword(), utilisateur.getMotDePasseHache())) {
            logger.warn("Login failed for user {}: Invalid credentials (password mismatch).", authRequestDTO.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }


        if (!utilisateur.isStatus()) {
            throw new UserNotActiveException("User is not active");
        }

        if (utilisateur.getCreatedAt().equals(utilisateur.getUpdatedAt())) {
            throw new PasswordChangeRequiredException("Password change required. Please update your password.");
        }

        if (BCrypt.checkpw(authRequestDTO.getPassword(), utilisateur.getMotDePasseHache())) {
            String accessToken = jwtUtil.generateAccessToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(), utilisateur.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getNomUtilisateur());

            utilisateur.setRefreshToken(refreshToken);
            utilisateurRepository.save(utilisateur);

            logger.info("Login successful for user: {}", authRequestDTO.getEmail());
            return new AuthResponseDTO(accessToken, refreshToken);
        } else {
            logger.warn("Login failed for user {}: Invalid credentials.", authRequestDTO.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @Override
    public AuthResponseDTO changePassword(ChangePasswordRequestDTO request) {
        logger.info("Attempting password change for email: {}", request.getEmail());
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!BCrypt.checkpw(request.getOldPassword(), utilisateur.getMotDePasseHache())) {
            logger.warn("Password change failed for email {}: Invalid old password.", request.getEmail());
            throw new InvalidOldPasswordException("Invalid old password.");
        }

        utilisateur.setMotDePasseHache(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        utilisateur.setUpdatedAt(Instant.now()); // Update updatedAt to reflect password change
        utilisateurRepository.save(utilisateur);
        logger.info("Password successfully changed for email: {}", request.getEmail());

        String accessToken = jwtUtil.generateAccessToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(), utilisateur.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getNomUtilisateur());

        utilisateur.setRefreshToken(refreshToken);
        utilisateurRepository.save(utilisateur);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        logger.info("Attempting to refresh access token.");
        try {
            Claims claims = jwtUtil.validateRefreshToken(refreshToken);
            String username = claims.getSubject();

            Utilisateur utilisateur = utilisateurRepository.findByNomUtilisateur(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            if (!refreshToken.equals(utilisateur.getRefreshToken())) {
                logger.warn("Refresh token mismatch for user: {}", username);
                throw new InvalidRefreshTokenException("Invalid refresh token.");
            }

            String newAccessToken = jwtUtil.generateAccessToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(), utilisateur.getEmail());
            logger.info("Access token successfully refreshed for user: {}", username);
            // Do not regenerate refresh token, use the existing one
            return new AuthResponseDTO(newAccessToken, refreshToken);
        } catch (UserNotFoundException e) {
            logger.error("Refresh token failed: User not found. {}", e.getMessage());
            throw e;
        } catch (InvalidRefreshTokenException e) {
            logger.error("Refresh token failed: Invalid refresh token. {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred during refresh token process: {}", e.getMessage());
            throw new InvalidRefreshTokenException("Invalid or expired refresh token : "+ e);
        }
    }
}
