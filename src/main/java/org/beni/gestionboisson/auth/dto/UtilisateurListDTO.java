package org.beni.gestionboisson.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurListDTO {
    
    private Long idUtilisateur;
    private String nomUtilisateur;
    private String email;
    private boolean status;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleDTO role;
}