package org.beni.gestionboisson.type_mouvement.repository;

import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;

import java.util.List;
import java.util.Optional;

public interface TypeMouvementRepository {
    Optional<TypeMouvement> findById(Long id);
    Optional<TypeMouvement> findByLibelle(String libelle);
    Optional<TypeMouvement> findByCode(String code);
    List<TypeMouvement> findAll();
    TypeMouvement save(TypeMouvement typeMouvement);
    void delete(TypeMouvement typeMouvement);
    boolean existsByLibelle(String libelle);
    boolean existsByCode(String code);
}
