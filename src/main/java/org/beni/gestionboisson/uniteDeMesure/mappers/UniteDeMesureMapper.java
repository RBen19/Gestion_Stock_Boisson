package org.beni.gestionboisson.uniteDeMesure.mappers;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;

public class UniteDeMesureMapper {

    public static UniteDeMesureDTO toDto(UniteDeMesure uniteDeMesure) {
        if (uniteDeMesure == null) {
            return null;
        }
        return UniteDeMesureDTO.builder()
                .id(uniteDeMesure.getId())
                .code(uniteDeMesure.getCode())
                .libelle(uniteDeMesure.getLibelle())
                .description(uniteDeMesure.getDescription())
                .build();
    }

    public static UniteDeMesure toEntity(UniteDeMesureDTO uniteDeMesureDTO) {
        if (uniteDeMesureDTO == null) {
            return null;
        }
        return UniteDeMesure.builder()
                .id(uniteDeMesureDTO.getId())
                .code(uniteDeMesureDTO.getCode())
                .libelle(uniteDeMesureDTO.getLibelle())
                .description(uniteDeMesureDTO.getDescription())
                .build();
    }

    public static void updateEntityFromDto(UniteDeMesureDTO dto, UniteDeMesure entity) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }
        if (dto.getLibelle() != null) {
            entity.setLibelle(dto.getLibelle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }
}
