package org.beni.gestionboisson.fournisseur.repository;

import org.beni.gestionboisson.fournisseur.entities.Fournisseur;

import java.util.List;
import java.util.Optional;

public interface FournisseurRepository {
    Optional<Fournisseur> findByCode(String code);
    List<Fournisseur> findAll();
    Fournisseur save(Fournisseur fournisseur);
    Optional<Fournisseur> findById(Long id);
    void deleteById(Long id);

}
