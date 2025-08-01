package org.beni.gestionboisson.lot.service;

import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.dto.LotSearchStrategyDTO;
import org.beni.gestionboisson.lot.dto.LotStatusUpdateDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des lots de boissons
 */
public interface LotService {
    /**
     * Crée un nouveau lot
     */
    LotResponseDTO createLot(LotDTO lotDTO);
    
    /**
     * Récupère un lot par son ID
     */
    LotResponseDTO getLotById(Long id);
    
    /**
     * Récupère un lot par son numéro
     */
    LotResponseDTO getLotByNumeroLot(String numeroLot);
    
    /**
     * Récupère tous les lots
     */
    List<LotResponseDTO> getAllLots();
    
    /**
     * Met à jour un lot existant
     */
    LotResponseDTO updateLot(Long id, LotDTO lotDTO);
    
    /**
     * Supprime un lot
     */
    void deleteLot(Long id);
    
    /**
     * Génère un numéro de lot unique
     */
    String generateLotNumero();
    
    /**
     * Vérifie la quantité disponible pour une boisson
     */
    void validateAvailableQuantity(String boissonCode, Double requestedQty);
    
    /**
     * Change le statut d'un lot
     */
    LotResponseDTO changeLotStatus(Long lotId, LotStatusUpdateDTO statusUpdateDTO);
    
    /**
     * Trouve les lots disponibles selon la stratégie FIFO
     */
    List<LotResponseDTO> findAvailableLotsFifo(String boissonCode, Optional<String> uniteDeMesureCode);
    
    /**
     * Trouve les lots disponibles selon la stratégie LIFO
     */
    List<LotResponseDTO> findAvailableLotsLifo(String boissonCode, Optional<String> uniteDeMesureCode);
    
    /**
     * Trouve les lots disponibles selon la stratégie FEFO
     */
    List<LotResponseDTO> findAvailableLotsFefo(String boissonCode, Optional<String> uniteDeMesureCode);
}
