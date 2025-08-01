package org.beni.gestionboisson.uniteDeMesure.service;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;

import java.util.List;

/**
 * Service pour la gestion des unités de mesure
 */
public interface UniteDeMesureService {
    /**
     * Récupère toutes les unités de mesure
     */
    List<UniteDeMesureDTO> getAllUnitesDeMesure();
    /**
     * Récupère une unité de mesure par son identifiant
     */
    UniteDeMesureDTO getUniteDeMesureById(Long id);
    /**
     * Crée une nouvelle unité de mesure
     */
    UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO);
    /**
     * Met à jour une unité de mesure existante
     */
    UniteDeMesureDTO updateUniteDeMesure(Long id, UniteDeMesureDTO uniteDeMesureDTO);
    /**
     * Supprime une unité de mesure
     */
    void deleteUniteDeMesure(Long id);
    /**
     * Initialise des unités de mesure par défaut
     */
    void seedUniteDeMesure();
}
