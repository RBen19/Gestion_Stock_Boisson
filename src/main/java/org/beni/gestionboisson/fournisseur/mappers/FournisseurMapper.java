package org.beni.gestionboisson.fournisseur.mappers;

import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.UpdateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;

public class FournisseurMapper {

    public static Fournisseur toEntity(CreateFournisseurDTO dto) {
        return Fournisseur.builder()
                .nom(dto.getNom())
                .emailContact(dto.getEmailContact())
                .numeroTelephone(dto.getNumeroTelephone())
                .build();
    }

    public static FournisseurDTO toDTO(Fournisseur entity) {
        return FournisseurDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .emailContact(entity.getEmailContact())
                .numeroTelephone(entity.getNumeroTelephone())
                .codeFournisseur(entity.getCodeFournisseur())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static void updateEntity(UpdateFournisseurDTO dto, Fournisseur entity) {
        entity.setNom(dto.getNom());
        entity.setEmailContact(dto.getEmailContact());
        entity.setNumeroTelephone(dto.getNumeroTelephone());
    }
}
