package org.beni.gestionboisson.boisson.service;

import org.beni.gestionboisson.boisson.dto.BoissonDTO;

import java.util.List;

/**
 * Service pour la gestion des boissons
 */
public interface BoissonService {
    /**
     * Crée une nouvelle boisson
     */
    BoissonDTO createBoisson(BoissonDTO dto);
    
    /**
     * Récupère toutes les boissons
     */
    List<BoissonDTO> getAllBoissons();
    
    /**
     * Récupère une boisson par son ID
     */
    BoissonDTO getBoissonById(Long id);
    
    /**
     * Initialise les données de test pour les boissons
     */
    void seedBoissons();
    
    /**
     * Récupère une boisson par son code
     */
    BoissonDTO getBoissonByCode(String codeBoisson);
    
    /**
     * Met à jour une boisson existante
     */
    BoissonDTO updateBoisson(Long id, BoissonDTO dto);
}
