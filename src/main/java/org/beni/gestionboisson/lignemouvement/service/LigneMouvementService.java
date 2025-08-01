package org.beni.gestionboisson.lignemouvement.service;

import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;

import java.util.List;

/**
 * Service pour la gestion des lignes de mouvements
 */
public interface LigneMouvementService {
    /**
     * Crée une nouvelle ligne de mouvement
     */
    LigneMouvementDTO createLigneMouvement(LigneMouvementCreateDTO ligneMouvementCreateDTO);
    /**
     * Récupère une ligne de mouvement par son identifiant
     */
    LigneMouvementDTO getLigneMouvementById(Long id);
    /**
     * Récupère toutes les lignes de mouvements
     */
    List<LigneMouvementDTO> getAllLigneMouvements();
    /**
     * Récupère les lignes de mouvements d'un mouvement spécifique
     */
    List<LigneMouvementDTO> getLigneMouvementsByMouvementId(Long mouvementId);
}
