package org.beni.gestionboisson.lignemouvement.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LigneMouvementDTO {
    private Long id;
    private Long mouvementId;
    private String lotCode;
    private Double quantite;
    private String emplacementSourceCode;
    private String emplacementDestinationCode;
    private String code;
    private BigDecimal prixUnitaire;
    private BigDecimal montantTotal;
    private Date createdAt;
    private Instant updatedAt;


}
