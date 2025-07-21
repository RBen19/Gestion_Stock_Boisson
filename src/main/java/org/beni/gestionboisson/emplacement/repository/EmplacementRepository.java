package org.beni.gestionboisson.emplacement.repository;

import org.beni.gestionboisson.emplacement.entities.Emplacement;

import java.util.List;
import java.util.Optional;

public interface EmplacementRepository {
    Optional<Emplacement> findByCodeEmplacement(String codeEmplacement);
    List<Emplacement> findAll();
    Emplacement save(Emplacement emplacement);
    Optional<Emplacement> findById(Long id);
    void deleteById(Long id);
    Optional<Emplacement> findEmplacementByNom(String nom);
}
