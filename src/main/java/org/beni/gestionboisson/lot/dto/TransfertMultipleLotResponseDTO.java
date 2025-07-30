package org.beni.gestionboisson.lot.dto;

import lombok.*;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransfertMultipleLotResponseDTO {
    private MouvementDTO mouvement; // Le mouvement principal créé
    private List<TransfertLotDetail> detailsTransferts; // Détails de chaque transfert effectué
    private Double quantiteTransfereTotale; // Quantité totale effectivement transférée
    private String message; // Message de succès ou d'information
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransfertLotDetail {
        private String numeroLotOriginal;
        private String numeroLotTransfere; // Peut être le même si transfert total
        private Double quantiteTransferee;
        private boolean transfertTotal; // true si tout le lot a été transféré
    }
}