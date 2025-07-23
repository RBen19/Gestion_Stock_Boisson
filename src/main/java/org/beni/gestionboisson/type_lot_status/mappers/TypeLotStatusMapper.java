package org.beni.gestionboisson.type_lot_status.mappers;

import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;

public class TypeLotStatusMapper {

    public static TypeLotStatusDTO toDTO(TypeLotStatus entity) {
        if (entity == null) {
            return null;
        }
        return TypeLotStatusDTO.builder()
                .id(entity.getId())
                .libelle(entity.getLibelle())
                .slug(entity.getSlug())
                .build();
    }

    public static TypeLotStatus toEntity(TypeLotStatusDTO dto) {
        if (dto == null) {
            return null;
        }
        return TypeLotStatus.builder()
                .id(dto.getId())
                .libelle(dto.getLibelle())
                .slug(dto.getSlug())
                .build();
    }
}
