package org.beni.gestionboisson.emplacement.service;

import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;

import java.util.List;

/**
 * Service pour la gestion des emplacements de stockage
 */
public interface EmplacementService {
    /**
     * Crée un nouvel emplacement de stockage
     */
    EmplacementDTO createEmplacement(EmplacementDTO dto);
    /**
     * Récupère tous les emplacements
     */
    List<EmplacementDTO> getAllEmplacements();
    /**
     * Récupère un emplacement par son code
     */
    EmplacementDTO getEmplacementByCode(String code);
    /**
     * Met à jour les informations d'un emplacement
     */
    EmplacementDTO updateEmplacement(String code, EmplacementDTO dto);
    /**
     * Supprime un emplacement
     */
    void deleteEmplacement(String code);
    /**
     * Initialise des emplacements par défaut dans la base de données
     */
    void seedEmplacements();
}
