package org.beni.gestionboisson.dashboard.service;

import org.beni.gestionboisson.dashboard.dto.DashboardStatsDTO;
import org.beni.gestionboisson.dashboard.dto.LotAnalyticsDTO;
import org.beni.gestionboisson.dashboard.dto.MouvementAnalyticsDTO;

/**
 * Service pour la gestion du tableau de bord et des statistiques
 */
 public interface DashboardService {
    /**
     * Récupère les statistiques générales du tableau de bord
     */
    DashboardStatsDTO getDashboardStats();
    /**
     * Récupère les statistiques d'analyse des lots
     */
    LotAnalyticsDTO getLotAnalytics();
    /**
     * Récupère les statistiques d'analyse des mouvements
     */
    MouvementAnalyticsDTO getMouvementAnalytics();
}

