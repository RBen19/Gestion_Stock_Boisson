package org.beni.gestionboisson.auth.mappers;

import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurListDTO;
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

    public static UtilisateurListDTO toListDto(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        return UtilisateurListDTO.builder()
                .idUtilisateur(utilisateur.getIdUtilisateur())
                .nomUtilisateur(utilisateur.getNomUtilisateur())
                .email(utilisateur.getEmail())
                .status(utilisateur.isStatus())
                .createdAt(utilisateur.getCreatedAt())
                .updatedAt(utilisateur.getUpdatedAt())
                .role(utilisateur.getRole() != null ? RoleMapper.toDto(utilisateur.getRole()) : null)
                .build();
    }
}
