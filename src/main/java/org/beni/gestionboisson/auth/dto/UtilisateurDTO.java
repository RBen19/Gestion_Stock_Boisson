package org.beni.gestionboisson.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {
    private Long idUtilisateur;
    private String nomUtilisateur;
    private String email;
    private String password;
    private String roleCode;
}
