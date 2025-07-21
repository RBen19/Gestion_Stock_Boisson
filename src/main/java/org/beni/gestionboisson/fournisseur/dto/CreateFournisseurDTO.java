package org.beni.gestionboisson.fournisseur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateFournisseurDTO {
    private String nom;
    private String emailContact;
    private String numeroTelephone;
}
