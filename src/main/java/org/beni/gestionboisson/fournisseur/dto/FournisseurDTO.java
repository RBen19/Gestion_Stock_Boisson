package org.beni.gestionboisson.fournisseur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FournisseurDTO {
    private Long id;
    private String nom;
    private String emailContact;
    private String numeroTelephone;
    private String codeFournisseur;
    private Boolean status;
    private Instant createdAt;
    private Instant updatedAt;
}
