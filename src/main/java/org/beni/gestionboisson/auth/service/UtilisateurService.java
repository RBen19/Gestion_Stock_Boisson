package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurListDTO;

import java.util.List;

/**
 * Service pour la gestion des utilisateurs
 */
public interface UtilisateurService {
    /**
     * Récupère un utilisateur par son ID
     */
    UtilisateurDTO getUtilisateurById(Long id);
    
    /**
     * Crée un nouvel utilisateur avec un rôle spécifique
     */
    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO, String roleCode);
    
    /**
     * Vérifie si un email existe déjà
     */
    boolean checkEmailExists(String email);
    
    /**
     * Vérifie si un nom d'utilisateur existe déjà
     */
    boolean checkNomUtilisateurExists(String nomUtilisateur);
    
    /**
     * Active ou désactive un utilisateur
     */
    UtilisateurDTO toggleUserStatus(String email);
    
    /**
     * Récupère la liste de tous les utilisateurs
     */
    List<UtilisateurListDTO> getAllUtilisateurs();
}
