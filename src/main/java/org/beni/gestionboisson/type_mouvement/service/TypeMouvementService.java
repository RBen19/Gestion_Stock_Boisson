package org.beni.gestionboisson.type_mouvement.service;

import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;

import java.util.List;

/**
 * Service pour la gestion des types de mouvements
 */
public interface TypeMouvementService {
    /**
     * Récupère tous les types de mouvements
     */
    List<TypeMouvementDTO> getAllTypeMouvements();
    /**
     * Récupère un type de mouvement par son identifiant
     */
    TypeMouvementDTO getTypeMouvementById(Long id);
    /**
     * Crée un nouveau type de mouvement
     */
    TypeMouvementDTO createTypeMouvement(TypeMouvementDTO typeMouvementDTO);
    /**
     * Met à jour un type de mouvement existant
     */
    TypeMouvementDTO updateTypeMouvement(Long id, TypeMouvementDTO typeMouvementDTO);
    /**
     * Supprime un type de mouvement
     */
    void deleteTypeMouvement(Long id);
    /**
     * Initialise des types de mouvements par défaut
     */
    void seedTypeMouvements();
}
