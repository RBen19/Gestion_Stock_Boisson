package org.beni.gestionboisson.boisson.repository;

import org.beni.gestionboisson.boisson.entities.Categorie;

import java.util.List;
import java.util.Optional;

public interface CategorieRepository {
    List<Categorie> findAll();
    Optional<Categorie> findById(Long id);
    Categorie save(Categorie categorie);
    void deleteById(Long id);
    Optional<Categorie> findByCode(String code);
}
