package org.beni.gestionboisson.type_emplacement.repository;

import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;

import java.util.List;
import java.util.Optional;

public interface TypeEmplacementRepository {
    Optional<TypeEmplacement> findByCode(String code);
    List<TypeEmplacement> findAll();
    TypeEmplacement save(TypeEmplacement typeEmplacement);
    Optional<TypeEmplacement> findById(Long id);
    void deleteById(Long id);

    Optional<TypeEmplacement> findByLibelle(String libelle);
}
