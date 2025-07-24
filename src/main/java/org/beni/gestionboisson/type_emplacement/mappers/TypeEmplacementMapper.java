package org.beni.gestionboisson.type_emplacement.mappers;

import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;

public class
TypeEmplacementMapper {

    public static TypeEmplacement toEntity(TypeEmplacementDTO dto) {
        return TypeEmplacement.builder()
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .build();
    }

    public static TypeEmplacementDTO toDTO(TypeEmplacement entity) {
        return TypeEmplacementDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .libelle(entity.getLibelle())
                .description(entity.getDescription())
                .build();
    }

    public static void updateEntity(TypeEmplacementDTO dto, TypeEmplacement entity) {
        entity.setCode(dto.getCode());
        entity.setLibelle(dto.getLibelle());
        entity.setDescription(dto.getDescription());
    }
}
