package org.beni.gestionboisson.type_mouvement.service;

import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;

import java.util.List;

public interface TypeMouvementService {
    List<TypeMouvementDTO> getAllTypeMouvements();
    TypeMouvementDTO getTypeMouvementById(Long id);
    TypeMouvementDTO createTypeMouvement(TypeMouvementDTO typeMouvementDTO);
    TypeMouvementDTO updateTypeMouvement(Long id, TypeMouvementDTO typeMouvementDTO);
    void deleteTypeMouvement(Long id);
    void seedTypeMouvements();
}
