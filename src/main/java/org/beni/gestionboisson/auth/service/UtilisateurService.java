package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurListDTO;

import java.util.List;

public interface UtilisateurService {
    UtilisateurDTO getUtilisateurById(Long id);
    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO, String roleCode);
    boolean checkEmailExists(String email);
    boolean checkNomUtilisateurExists(String nomUtilisateur);
    UtilisateurDTO toggleUserStatus(String email);
    List<UtilisateurListDTO> getAllUtilisateurs();
}
