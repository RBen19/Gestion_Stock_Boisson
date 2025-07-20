package org.beni.gestionboisson.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long idRole;
    private String code;
    private String libelle;
}
