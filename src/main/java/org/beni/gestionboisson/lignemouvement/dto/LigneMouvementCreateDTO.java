package org.beni.gestionboisson.lignemouvement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LigneMouvementCreateDTO {
    @NotNull(message = "Mouvement ID cannot be null")
    private Long mouvementId;

    @NotBlank(message = "Lot code cannot be blank")
    private String lotCode;

    @NotNull(message = "Quantite cannot be null")
    private Double quantite;

    private String emplacementSourceCode;
    private String emplacementDestinationCode;

    @NotNull(message = "Prix unitaire cannot be null")
    private BigDecimal prixUnitaire;


}
