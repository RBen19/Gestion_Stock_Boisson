package org.beni.gestionboisson.uniteDeMesure.service;

import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;

import java.util.List;

public interface UniteDeMesureService {
    UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO);
    List<UniteDeMesureDTO> getAllUniteDeMesures();
    UniteDeMesureDTO getUniteDeMesureById(Long id);
    UniteDeMesureDTO getUniteDeMesureByCode(String code);
    void deleteUniteDeMesure(Long id);
    void seedUniteDeMesure();
}
