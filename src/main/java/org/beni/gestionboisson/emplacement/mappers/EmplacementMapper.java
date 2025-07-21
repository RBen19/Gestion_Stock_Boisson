package org.beni.gestionboisson.emplacement.mappers;

import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
public class EmplacementMapper {

    public static Emplacement toEntity(EmplacementDTO dto) {
        return Emplacement.builder()
                .nom(dto.getNom())
                .build();
    }

    public static EmplacementDTO toDTO(Emplacement entity) {
        return EmplacementDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .codeEmplacement(entity.getCodeEmplacement())
                .codeTypeEmplacement(entity.getType() != null ? entity.getType().getCode() : null)
                .build();
    }

    public static void updateEntity(EmplacementDTO dto, Emplacement entity) {
        entity.setNom(dto.getNom());
    }
}
