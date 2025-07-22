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
public class BoissonDTO {
    private Long id;
   // private String nom;
    private String nom;
    private String codeBoisson;
    
    private String codeCategorie;
  //  private Instant createdAt;
  //  private Instant updatedAt;
}
