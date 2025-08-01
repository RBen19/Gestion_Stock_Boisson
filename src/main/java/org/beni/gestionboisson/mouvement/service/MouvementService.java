package org.beni.gestionboisson.mouvement.service;

import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;
import org.beni.gestionboisson.lot.dto.TransfertMultipleLotDTO;
import org.beni.gestionboisson.lot.dto.TransfertMultipleLotResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des mouvements de stock
 */
public interface MouvementService {
    /**
     * Crée un nouveau mouvement de stock
     */
    MouvementDTO createMouvement(MouvementCreateDTO mouvementCreateDTO);
    /**
     * Récupère un mouvement par son identifiant
     */
    MouvementDTO getMouvementById(Long id);
    /**
     * Récupère tous les mouvements de stock
     */
    List<MouvementDTO> getAllMouvements();
    /**
     * Réceptionne un lot dans un emplacement
     */
    MouvementDTO receptionnerLot(Long lotId, Double quantite, String codeEmplacementDestination, String utilisateurEmail, String notes);
    /**
     * Transfère un lot d'un emplacement vers un autre
     */
    MouvementDTO transfertLot(String numeroLot, Double quantite,String codeEmplamcementSouce, String codeEmplacementDestination,String UtilisateurEmail, String notes);
    /**
     * Transfère plusieurs lots en une seule opération
     */
    TransfertMultipleLotResponseDTO transfertMultipleLots(TransfertMultipleLotDTO transfertMultipleLotDTO);
    /**
     * Compte le nombre total de mouvements
     */
    Integer getAllMouvementCount();
}
