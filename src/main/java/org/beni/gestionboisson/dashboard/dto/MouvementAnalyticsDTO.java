package org.beni.gestionboisson.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MouvementAnalyticsDTO {
    private long mouvementsCreatedToday;
    private long mouvementsCreatedThisWeek;
    private long mouvementsCreatedThisMonth;
    private long mouvementsCreatedThisYear;
    private Map<String, Long> quantityMovedPerBoisson;
    private Map<String, Double> top5MostMovedBoissons;
    private BigDecimal totalValueMoved;
}
