package org.beni.gestionboisson.boisson.repository;

import org.beni.gestionboisson.boisson.entities.Boisson;

import java.util.List;
import java.util.Optional;

public interface BoissonRepository {
    List<Boisson> findAll();
    Optional<Boisson> findById(Long id);
    Boisson save(Boisson boisson);
    void deleteById(Long id);
    long countByCategoryAndUnit(String categoryCode, String unit);
    Optional<Long> findMaxId();
    Optional<Boisson> getBoissonByCode(String codeBoisson);
}
