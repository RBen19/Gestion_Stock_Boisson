package org.beni.gestionboisson.fournisseur.service;

import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.UpdateFournisseurDTO;

import java.util.List;

/**
 * Service pour la gestion des fournisseurs
 */
public interface FournisseurService {
    /**
     * Crée un nouveau fournisseur
     */
    FournisseurDTO createFournisseur(CreateFournisseurDTO dto);
    /**
     * Récupère tous les fournisseurs
     */
    List<FournisseurDTO> getAllFournisseurs();
    /**
     * Récupère un fournisseur par son code
     */
    FournisseurDTO getFournisseurByCode(String code);
    /**
     * Met à jour les informations d'un fournisseur
     */
    FournisseurDTO updateFournisseur(String code, UpdateFournisseurDTO dto);
    /**
     * Change le statut actif/inactif d'un fournisseur
     */
    void changeStatusByCode(String code, boolean newStatus);
}
