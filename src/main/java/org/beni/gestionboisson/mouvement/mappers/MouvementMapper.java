package org.beni.gestionboisson.mouvement.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;
import org.beni.gestionboisson.mouvement.entities.Mouvement;

@ApplicationScoped
public class MouvementMapper {

    public static MouvementDTO toDTO(Mouvement mouvement) {
        if (mouvement == null) {
            return null;
        }
        MouvementDTO dto = new MouvementDTO();
        dto.setId(mouvement.getId());
        dto.setNotes(mouvement.getNotes());
        dto.setCode(mouvement.getCode());
        dto.setCreatedAt(mouvement.getCreatedAt());
        if (mouvement.getTypeMouvement() != null) {
            dto.setTypeMouvementCode(mouvement.getTypeMouvement().getCode());
        }
        if (mouvement.getUtilisateur() != null) {
            dto.setUtilisateurEmail(mouvement.getUtilisateur().getEmail());
        }
        return dto;
    }

    public static Mouvement toEntity(MouvementCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        Mouvement mouvement = new Mouvement();
        mouvement.setNotes(dto.getNotes());
        // TypeMouvement and Utilisateur will be set in the service layer based on codes
        return mouvement;
    }
}
