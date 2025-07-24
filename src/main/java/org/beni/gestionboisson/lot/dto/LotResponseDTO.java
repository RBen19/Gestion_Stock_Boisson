package org.beni.gestionboisson.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotResponseDTO {
    private Long id;
    private String numeroLot;
    private BoissonDTO boisson;
    private Instant dateAcquisition;
    private Instant dateLimiteConsommation;
    private Double quantiteInitiale;
    private Double quantiteActuelle;
    private FournisseurDTO fournisseur;
    private TypeLotStatusDTO typeLotStatus;
    private Instant createdAt;
    private Instant updatedAt;
}
