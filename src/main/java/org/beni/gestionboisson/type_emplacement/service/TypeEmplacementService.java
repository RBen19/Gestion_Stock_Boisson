package org.beni.gestionboisson.type_emplacement.service;

import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;

import java.util.List;

public interface TypeEmplacementService {
    TypeEmplacementDTO createTypeEmplacement(TypeEmplacementDTO dto);
    List<TypeEmplacementDTO> getAllTypeEmplacements();
    TypeEmplacementDTO getTypeEmplacementByCode(String code);
    TypeEmplacementDTO updateTypeEmplacement(String code, TypeEmplacementDTO dto);
    void deleteTypeEmplacement(String code);
    void seedTypeEmplacements();
}