package org.beni.gestionboisson.lot.mappers;

import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.entities.Lot;

public class LotMapper {

    public static LotResponseDTO toDTO(Lot lot) {
        if (lot == null) {
            return null;
        }
        return LotResponseDTO.builder()
                .id(lot.getId())
                .numeroLot(lot.getNumeroLot())
                .boisson(null) // Will be mapped in service layer
                .dateAcquisition(lot.getDateAcquisition())
                .dateLimiteConsommation(lot.getDateLimiteConsommation())
                .quantiteInitiale(lot.getQuantiteInitiale())
                .quantiteActuelle(lot.getQuantiteActuelle())
                .fournisseur(null) // Will be mapped in service layer
                
                .typeLotStatus(null) // Will be mapped in service layer
                .createdAt(lot.getCreatedAt())
                .updatedAt(lot.getUpdatedAt())
                .build();
    }

    public static Lot toEntity(LotDTO lotDTO) {
        if (lotDTO == null) {
            return null;
        }
        return Lot.builder()
                .dateAcquisition(lotDTO.getDateAcquisition())
                .dateLimiteConsommation(lotDTO.getDateLimiteConsommation())
                .quantiteInitiale(lotDTO.getQuantiteInitiale())
                .quantiteActuelle(lotDTO.getQuantiteActuelle())
                .codeEmplacementActuel(lotDTO.getCodeEmplacementActuel())
                .build();
    }
}
