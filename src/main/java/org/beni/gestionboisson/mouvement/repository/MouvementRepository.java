package org.beni.gestionboisson.mouvement.repository;

import org.beni.gestionboisson.mouvement.entities.Mouvement;

import java.util.Optional;
import java.util.List;


public interface MouvementRepository {
    Mouvement save(Mouvement mouvement);
    Optional<Mouvement> findById(Long id);
    Optional<Mouvement> findByCode(String code);
    List<Mouvement> findAll();
    Integer getAllMouvementCount();
}
