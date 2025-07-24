package org.beni.gestionboisson.lot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotSearchStrategyDTO {
    private String boissonCode;
    private Optional<String> uniteDeMesureCode;
}
