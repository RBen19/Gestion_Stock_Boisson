package org.beni.gestionboisson.uniteDeMesure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniteDeMesureDTO {
    private Long id;
    private String code;
    private String libelle;
    private String description;
}
