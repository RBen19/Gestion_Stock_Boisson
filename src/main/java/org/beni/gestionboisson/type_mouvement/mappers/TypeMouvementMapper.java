package org.beni.gestionboisson.type_mouvement.mappers;

import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;

public class TypeMouvementMapper {

    public static TypeMouvementDTO toDTO(TypeMouvement typeMouvement) {
        if (typeMouvement == null) {
            return null;
        }
        return TypeMouvementDTO.builder()
                .id(typeMouvement.getId())
                .libelle(typeMouvement.getLibelle())
                .code(typeMouvement.getCode())
                .build();
    }

    public static TypeMouvement toEntity(TypeMouvementDTO typeMouvementDTO) {
        if (typeMouvementDTO == null) {
            return null;
        }
        return TypeMouvement.builder()
                .id(typeMouvementDTO.getId())
                .libelle(typeMouvementDTO.getLibelle())
                .code(typeMouvementDTO.getCode())
                .build();
    }
}
