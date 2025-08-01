package org.beni.gestionboisson.type_emplacement.service;

import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;

import java.util.List;

/**
 * Service pour la gestion des types d'emplacements
 */
public interface TypeEmplacementService {
    /**
     * Crée un nouveau type d'emplacement
     */
    TypeEmplacementDTO createTypeEmplacement(TypeEmplacementDTO dto);
    /**
     * Récupère tous les types d'emplacements
     */
    List<TypeEmplacementDTO> getAllTypeEmplacements();
    /**
     * Récupère un type d'emplacement par son code
     */
    TypeEmplacementDTO getTypeEmplacementByCode(String code);
    /**
     * Met à jour un type d'emplacement
     */
    TypeEmplacementDTO updateTypeEmplacement(String code, TypeEmplacementDTO dto);
    /**
     * Supprime un type d'emplacement
     */
    void deleteTypeEmplacement(String code);
    /**
     * Initialise des types d'emplacements par défaut
     */
    void seedTypeEmplacements();
}