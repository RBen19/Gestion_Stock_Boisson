package org.beni.gestionboisson.lignemouvement.repository;

import org.beni.gestionboisson.lignemouvement.entities.LigneMouvement;

import java.util.List;
import java.util.Optional;

public interface LigneMouvementRepository {
    LigneMouvement save(LigneMouvement ligneMouvement);
    Optional<LigneMouvement> findById(Long id);
    Optional<LigneMouvement> findByCode(String code);
    List<LigneMouvement> findAll();
    List<LigneMouvement> findByMouvementId(Long mouvementId);
}
