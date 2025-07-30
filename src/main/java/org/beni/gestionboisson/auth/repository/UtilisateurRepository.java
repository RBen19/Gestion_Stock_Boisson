package org.beni.gestionboisson.auth.repository;

import org.beni.gestionboisson.auth.entities.Utilisateur;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository {
    Optional<Utilisateur> findById(Long id);
    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);
    Optional<Utilisateur> findByEmail(String email);
    Utilisateur save(Utilisateur utilisateur);
    List<Utilisateur> findAll();
}
