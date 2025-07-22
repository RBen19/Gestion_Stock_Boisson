package org.beni.gestionboisson.uniteDeMesure.mappers;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;

public class UniteDeMesureMapper {

    public static UniteDeMesureDTO toDTO(UniteDeMesure uniteDeMesure) {
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

    public static UniteDeMesure toEntity(UniteDeMesureDTO dto) {
        if (dto == null) {
            return null;
        }
        return UniteDeMesure.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .build();
    }
}
