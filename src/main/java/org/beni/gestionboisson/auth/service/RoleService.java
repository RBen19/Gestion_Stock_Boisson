package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.RoleDTO;

import java.util.List;

/**
 * Service pour la gestion des rôles utilisateur
 */
public interface RoleService {
    /**
     * Récupère tous les rôles
     */
    List<RoleDTO> getAllRoles();
    /**
     * Récupère un rôle par son identifiant
     */
    RoleDTO getRoleById(Long id);
    /**
     * Crée un nouveau rôle
     */
    RoleDTO createRole(RoleDTO roleDTO);
    /**
     * Met à jour un rôle existant
     */
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    /**
     * Supprime un rôle
     */
    void deleteRole(Long id);
}
