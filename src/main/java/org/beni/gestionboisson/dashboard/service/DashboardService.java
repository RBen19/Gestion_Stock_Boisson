package org.beni.gestionboisson.dashboard.service;

import org.beni.gestionboisson.dashboard.dto.DashboardStatsDTO;
import org.beni.gestionboisson.dashboard.dto.LotAnalyticsDTO;
import org.beni.gestionboisson.dashboard.dto.MouvementAnalyticsDTO;

 public interface DashboardService {
    DashboardStatsDTO getDashboardStats();
    LotAnalyticsDTO getLotAnalytics();
    MouvementAnalyticsDTO getMouvementAnalytics();
}

