package org.beni.gestionboisson.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalUtilisateurs;
    private long totalRoles;
    private long totalBoissons;
    private long totalCategories;
    private long totalUnitesDeMesure;
    private long totalFournisseurs;
    private long totalEmplacements;
    private long totalTypeMouvements;
    private long totalLots;
    private long totalTypeLotStatus;
}