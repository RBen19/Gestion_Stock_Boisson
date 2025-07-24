package org.beni.gestionboisson.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
    private String boissonCode;
    private LocalDate dateAcquisition;
    private LocalDate dateLimiteConsommation;
    private Double quantiteInitiale;
    private Double quantiteActuelle;
    private String fournisseurCode;
    private String typeLotStatusCode;
    private String utilisateurEmail;
    private String notes;
    private String codeEmplacementDestination;
    private String codeEmplacementActuel;
}
