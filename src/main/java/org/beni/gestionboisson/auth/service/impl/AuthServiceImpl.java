package org.beni.gestionboisson.auth.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;
import org.beni.gestionboisson.auth.exceptions.UserNotActiveException;
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
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        logger.warn("Hashed password from DB for {}: {}", authRequestDTO.getEmail(), utilisateur.getMotDePasseHache());


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

            return new AuthResponseDTO(accessToken, refreshToken);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public AuthResponseDTO changePassword(ChangePasswordRequestDTO request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!BCrypt.checkpw(request.getOldPassword(), utilisateur.getMotDePasseHache())) {
            throw new RuntimeException("Invalid old password.");
        }

        utilisateur.setMotDePasseHache(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        utilisateur.setUpdatedAt(Instant.now()); // Update updatedAt to reflect password change
        utilisateurRepository.save(utilisateur);

        String accessToken = jwtUtil.generateAccessToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(), utilisateur.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getNomUtilisateur());

        utilisateur.setRefreshToken(refreshToken);
        utilisateurRepository.save(utilisateur);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        try {
            Claims claims = jwtUtil.validateRefreshToken(refreshToken);
            String username = claims.getSubject();

            Utilisateur utilisateur = utilisateurRepository.findByNomUtilisateur(username)
                    .orElseThrow(() -> new RuntimeException("User not found."));

            if (!refreshToken.equals(utilisateur.getRefreshToken())) {
                throw new RuntimeException("Invalid refresh token.");
            }

            String newAccessToken = jwtUtil.generateAccessToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(), utilisateur.getEmail());

            // Do not regenerate refresh token, use the existing one
            return new AuthResponseDTO(newAccessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired refresh token.", e);
        }
    }
}
