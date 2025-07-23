package org.beni.gestionboisson.type_lot_status.service;

import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;

import java.util.List;

public interface TypeLotStatusService {
    TypeLotStatusDTO createTypeLotStatus(TypeLotStatusDTO typeLotStatusDTO);
    TypeLotStatusDTO updateTypeLotStatus(Long id, TypeLotStatusDTO typeLotStatusDTO);
    void deleteTypeLotStatus(Long id);
    TypeLotStatusDTO getTypeLotStatusById(Long id);
    TypeLotStatusDTO getTypeLotStatusBySlug(String slug);
    List<TypeLotStatusDTO> getAllTypeLotStatuses();
    void seedTypeLotStatuses();
}
