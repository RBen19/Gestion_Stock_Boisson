package org.beni.gestionboisson.type_emplacement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeEmplacementDTO {
    private Long id;
    private String code;
    private String libelle;
    private String description;
}
