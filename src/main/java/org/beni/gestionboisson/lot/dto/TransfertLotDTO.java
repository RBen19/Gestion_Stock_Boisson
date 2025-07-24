package org.beni.gestionboisson.lot.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransfertLotDTO {
    private String numeroLot;
    private String codeEmplacementDestination;
    private String notes;
    private String UtilisateurEmail;
    private Double quantite;

}
