package org.beni.gestionboisson.uniteDeMesure.service;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;

import java.util.List;

public interface UniteDeMesureService {
    List<UniteDeMesureDTO> getAllUnitesDeMesure();
    UniteDeMesureDTO getUniteDeMesureById(Long id);
    UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO);
    UniteDeMesureDTO updateUniteDeMesure(Long id, UniteDeMesureDTO uniteDeMesureDTO);
    void deleteUniteDeMesure(Long id);
    void seedUniteDeMesure();
}
