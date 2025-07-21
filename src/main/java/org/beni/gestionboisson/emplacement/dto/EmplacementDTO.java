package org.beni.gestionboisson.emplacement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmplacementDTO {
    private Long id;
    private String nom;
    private String codeEmplacement;
    private String codeTypeEmplacement;
}
