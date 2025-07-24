package org.beni.gestionboisson.mouvement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MouvementCreateDTO {
    @NotBlank(message = "TypeMouvement code cannot be blank")
    private String typeMouvementCode;

    private String notes;

    @NotBlank(message = "Utilisateur code cannot be blank")
    private String utilisateurEmail;


}
