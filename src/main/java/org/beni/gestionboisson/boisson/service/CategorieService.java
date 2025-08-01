package org.beni.gestionboisson.boisson.service;

import org.beni.gestionboisson.boisson.dto.CategorieDTO;

import java.util.List;

/**
 * Service pour la gestion des catégories de boissons
 */
public interface CategorieService {
    /**
     * Récupère toutes les catégories
     */
    List<CategorieDTO> getAllCategories();
    /**
     * Récupère une catégorie par son identifiant
     */
    CategorieDTO getCategorieById(Long id);
    /**
     * Crée une nouvelle catégorie
     */
    CategorieDTO createCategorie(CategorieDTO categorieDTO);
    /**
     * Met à jour une catégorie existante
     */
    CategorieDTO updateCategorie(Long id, CategorieDTO categorieDTO);
    /**
     * Supprime une catégorie
     */
    void deleteCategorie(Long id);
    /**
     * Récupère une catégorie par son code
     */
    CategorieDTO getCategorieByCode(String codeCategorie);
    /**
     * Initialise des catégories par défaut dans la base de données
     */
    void seedCategories();
}
