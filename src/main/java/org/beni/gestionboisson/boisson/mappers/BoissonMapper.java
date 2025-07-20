package org.beni.gestionboisson.boisson.mappers;

import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.entities.Categorie;

public class BoissonMapper {

    public static BoissonDTO toDTO(Boisson boisson) {
        if (boisson == null) {
            return null;
        }
        return BoissonDTO.builder()
                .id(boisson.getId())
                .nom(boisson.getNom())
                .codeBoisson(boisson.getCodeBoisson())
                .codeCategorie(boisson.getCodeCategorie())
                .uniteDeMesure(boisson.getUniteDeMesure())
                .codeCategorie(boisson.getCodeCategorie())
              //  .idCategorie(boisson.getCategorie() != null ? boisson.getCategorie().getIdCategorie() : null)
               // .createdAt(boisson.getCreatedAt())
             //   .updatedAt(boisson.getUpdatedAt())
                .build();
    }

    public static Boisson toEntity(BoissonDTO dto) {
        if (dto == null) {
            return null;
        }
        Boisson boisson = Boisson.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .codeBoisson(dto.getCodeBoisson())
                .codeCategorie(dto.getCodeCategorie())
                .uniteDeMesure(dto.getUniteDeMesure())
               // .createdAt(dto.getCreatedAt())
           //     .updatedAt(dto.getUpdatedAt())
                .build();
        // Note: Setting the Categorie entity requires a database lookup and is handled in the service layer.
        return boisson;
    }
}
