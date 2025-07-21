package org.beni.gestionboisson.emplacement.service;

import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;

import java.util.List;

public interface EmplacementService {
    EmplacementDTO createEmplacement(EmplacementDTO dto);
    List<EmplacementDTO> getAllEmplacements();
    EmplacementDTO getEmplacementByCode(String code);
    EmplacementDTO updateEmplacement(String code, EmplacementDTO dto);
    void deleteEmplacement(String code);
    void seedEmplacements();
}
