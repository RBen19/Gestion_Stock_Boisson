package org.beni.gestionboisson.type_lot_status.service;

import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;

import java.util.List;

/**
 * Service pour la gestion des statuts de lots
 */
public interface TypeLotStatusService {
    /**
     * Crée un nouveau statut de lot
     */
    TypeLotStatusDTO createTypeLotStatus(TypeLotStatusDTO typeLotStatusDTO);
    /**
     * Met à jour un statut de lot existant
     */
    TypeLotStatusDTO updateTypeLotStatus(Long id, TypeLotStatusDTO typeLotStatusDTO);
    /**
     * Supprime un statut de lot
     */
    void deleteTypeLotStatus(Long id);
    /**
     * Récupère un statut de lot par son identifiant
     */
    TypeLotStatusDTO getTypeLotStatusById(Long id);
    /**
     * Récupère un statut de lot par son slug
     */
    TypeLotStatusDTO getTypeLotStatusBySlug(String slug);
    /**
     * Récupère tous les statuts de lots
     */
    List<TypeLotStatusDTO> getAllTypeLotStatuses();
    /**
     * Initialise des statuts de lots par défaut
     */
    void seedTypeLotStatuses();
}
