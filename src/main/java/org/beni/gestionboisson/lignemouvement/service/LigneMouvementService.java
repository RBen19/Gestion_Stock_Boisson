package org.beni.gestionboisson.lignemouvement.service;

import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;

import java.util.List;

public interface LigneMouvementService {
    LigneMouvementDTO createLigneMouvement(LigneMouvementCreateDTO ligneMouvementCreateDTO);
    LigneMouvementDTO getLigneMouvementById(Long id);
    List<LigneMouvementDTO> getAllLigneMouvements();
    List<LigneMouvementDTO> getLigneMouvementsByMouvementId(Long mouvementId);
}
