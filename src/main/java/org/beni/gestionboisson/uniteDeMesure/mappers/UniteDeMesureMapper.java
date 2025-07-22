package org.beni.gestionboisson.uniteDeMesure.mappers;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UniteDeMesureMapper {

    UniteDeMesureMapper INSTANCE = Mappers.getMapper(UniteDeMesureMapper.class);

    UniteDeMesureDTO toDto(UniteDeMesure uniteDeMesure);
    UniteDeMesure toEntity(UniteDeMesureDTO uniteDeMesureDTO);

    void updateEntityFromDto(UniteDeMesureDTO dto, @MappingTarget UniteDeMesure entity);
}