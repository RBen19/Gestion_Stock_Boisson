package org.beni.gestionboisson.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotStatusUpdateDTO {
    private String newStatusLibelle;
}
