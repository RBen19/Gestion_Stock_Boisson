package org.beni.gestionboisson.lot.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransfertMultipleLotDTO {
    private List<LotResponseDTO> lots; // Liste des lots ordonnés selon FIFO/LIFO/FEFO
    private Double quantiteTotaleDesire; // Quantité totale que l'utilisateur veut transférer
    private String codeEmplacementDestination;
    private String utilisateurEmail;
    private String notes;
    private String boissonCode; // Pour validation
}