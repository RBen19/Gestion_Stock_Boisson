package org.beni.gestionboisson.mouvement.service;

import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;

import java.util.List;
import java.util.Optional;

public interface MouvementService {
    MouvementDTO createMouvement(MouvementCreateDTO mouvementCreateDTO);
    MouvementDTO getMouvementById(Long id);
    List<MouvementDTO> getAllMouvements();
    MouvementDTO receptionnerLot(Long lotId, Double quantite, String codeEmplacementDestination, String utilisateurEmail, String notes);
    Integer getAllMouvementCount();
}
