package org.beni.gestionboisson.mouvement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MouvementDTO {
    private Long id;
    private String typeMouvementCode;
    private String notes;
    private String code;
    private String utilisateurEmail;
    private Instant createdAt;

}
