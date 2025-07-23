package org.beni.gestionboisson.type_lot_status.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeLotStatusDTO {

    private Long id;

    @NotBlank(message = "Libelle cannot be blank")
    @Size(max = 255, message = "Libelle cannot exceed 255 characters")
    private String libelle;

    private String slug;
}
