package org.beni.gestionboisson.uniteDeMesure.repository;

import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;

import java.util.List;
import java.util.Optional;

public interface UniteDeMesureRepository {
    List<UniteDeMesure> findAll();
    Optional<UniteDeMesure> findById(Long id);
    Optional<UniteDeMesure> findByCode(String code);
    UniteDeMesure save(UniteDeMesure uniteDeMesure);
    void deleteById(Long id);

    Optional<UniteDeMesure> findByLibelle(String libelle);
}
