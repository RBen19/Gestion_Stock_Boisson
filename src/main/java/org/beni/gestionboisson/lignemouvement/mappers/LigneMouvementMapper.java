package org.beni.gestionboisson.lignemouvement.mappers;

import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;
import org.beni.gestionboisson.lignemouvement.entities.LigneMouvement;


public  final class LigneMouvementMapper {
    private LigneMouvementMapper() {
        // empêche l'instanciation
    }
    public static LigneMouvementDTO toDTO(LigneMouvement ligneMouvement) {
        if (ligneMouvement == null) {
            return null;
        }
        LigneMouvementDTO dto =  LigneMouvementDTO.builder()
                .id(ligneMouvement.getId())
                .quantite(ligneMouvement.getQuantite())
                .code(ligneMouvement.getCode())
                .prixUnitaire(ligneMouvement.getPrixUnitaire())
                .montantTotal(ligneMouvement.getMontantTotal())
                .build();


        if (ligneMouvement.getMouvement() != null) {
            dto.setMouvementId(ligneMouvement.getMouvement().getId());
        }
        if (ligneMouvement.getLot() != null) {
            dto.setLotCode(ligneMouvement.getLot().getNumeroLot());
        }
        if (ligneMouvement.getEmplacementSource() != null) {
            dto.setEmplacementSourceCode(ligneMouvement.getEmplacementSource().getCodeEmplacement());
        }
        if (ligneMouvement.getEmplacementDestination() != null) {
            dto.setEmplacementDestinationCode(ligneMouvement.getEmplacementDestination().getCodeEmplacement());
        }
        return dto;
    }

    public static LigneMouvement toEntity(LigneMouvementCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        LigneMouvement ligneMouvement = LigneMouvement.builder().
                quantite(dto.getQuantite())
                        .prixUnitaire(dto.getPrixUnitaire()).build();


        // Mouvement, Lot, EmplacementSource, EmplacementDestination will be set in the service layer based on codes/ids
        return ligneMouvement;
    }
}
