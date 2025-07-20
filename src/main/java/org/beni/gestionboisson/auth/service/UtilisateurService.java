package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.UtilisateurDTO;

public interface UtilisateurService {
    UtilisateurDTO getUtilisateurById(Long id);
    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO, String roleCode);
    boolean checkEmailExists(String email);
    boolean checkNomUtilisateurExists(String nomUtilisateur);
}
