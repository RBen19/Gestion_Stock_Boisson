package org.beni.gestionboisson.boisson.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {
    private Long idCategorie;
    private String nom;
    
    private Long parentCategorieId;
   // private Instant createdAt;
   //
    // private Instant updatedAt;
}
