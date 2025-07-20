package org.beni.gestionboisson.auth.mappers;

import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.entities.Utilisateur;

public class UtilisateurMapper {

    public static UtilisateurDTO toDto(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UtilisateurDTO.builder()
                .idUtilisateur(utilisateur.getIdUtilisateur())
                .nomUtilisateur(utilisateur.getNomUtilisateur())
                .email(utilisateur.getEmail())
                .roleCode(utilisateur.getRole() != null ? utilisateur.getRole().getCode() : null)
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDTO utilisateurDTO) {
        if (utilisateurDTO == null) {
            return null;
        }
        return Utilisateur.builder()
                .idUtilisateur(utilisateurDTO.getIdUtilisateur())
                .nomUtilisateur(utilisateurDTO.getNomUtilisateur())
                .email(utilisateurDTO.getEmail())
                .build();
    }
}
