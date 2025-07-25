package org.beni.gestionboisson.lot.mappers;

import org.beni.gestionboisson.boisson.mappers.BoissonMapper;
import org.beni.gestionboisson.fournisseur.mappers.FournisseurMapper;
import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.type_lot_status.mappers.TypeLotStatusMapper;

public class LotMapper {

    public static LotResponseDTO toDTO(Lot lot) {
        if (lot == null) {
            return null;
        }
        return LotResponseDTO.builder()
                .id(lot.getId())
                .numeroLot(lot.getNumeroLot())
                .boisson(BoissonMapper.toDTO(lot.getBoisson()))
                .dateAcquisition(lot.getDateAcquisition())
                .dateLimiteConsommation(lot.getDateLimiteConsommation())
                .quantiteInitiale(lot.getQuantiteInitiale())
                .quantiteActuelle(lot.getQuantiteActuelle())
                .fournisseur(FournisseurMapper.toDTO(lot.getFournisseur()))
                .typeLotStatus(TypeLotStatusMapper.toDTO(lot.getTypeLotStatus()))
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
