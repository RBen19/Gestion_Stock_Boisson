package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;

/**
 * Service pour la gestion de l'authentification des utilisateurs
 */
public interface AuthService {
    /**
     * Connecte un utilisateur avec ses identifiants
     */
    AuthResponseDTO login(AuthRequestDTO authRequestDTO) throws PasswordChangeRequiredException;
    
    /**
     * Change le mot de passe d'un utilisateur
     */
    AuthResponseDTO changePassword(ChangePasswordRequestDTO request);
    
    /**
     * Renouvelle le token d'accès avec le refresh token
     */
    AuthResponseDTO refreshAccessToken(String refreshToken);
}
