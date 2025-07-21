package org.beni.gestionboisson.boisson.mappers;

import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;

public class CategorieMapper {

    public static CategorieDTO toDTO(Categorie categorie) {
        if (categorie == null) {
            return null;
        }
        return CategorieDTO.builder()
                .idCategorie(categorie.getIdCategorie())
                .nom(categorie.getNom())
                
                .parentCategorieId(categorie.getParentCategorie() != null ? categorie.getParentCategorie().getIdCategorie() : null)
             //   .createdAt(categorie.getCreatedAt())
            //    .updatedAt(categorie.getUpdatedAt())
                .build();
    }

    public static Categorie toEntity(CategorieDTO dto) {
        if (dto == null) {
            return null;
        }
        Categorie categorie = Categorie.builder()
                .idCategorie(dto.getIdCategorie())
                .nom(dto.getNom())
                
              //  .createdAt(dto.getCreatedAt())
              //  .updatedAt(dto.getUpdatedAt())
                .build();
        // Note: Setting the parent Categorie entity requires a database lookup and is handled in the service layer.
        return categorie;
    }
}
