package org.beni.gestionboisson.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotAnalyticsDTO {
    private long lotsCreatedToday;
    private long lotsCreatedThisWeek;
    private long lotsCreatedThisMonth;
    private long lotsCreatedThisYear;
    private Map<String, Long> lotsByStatus;
    private Map<String, Long> lotsPerBoisson;
    private Double totalQuantityReceivedThisMonth;
    private Map<String, Long> quantityAvailablePerBoisson;
}
