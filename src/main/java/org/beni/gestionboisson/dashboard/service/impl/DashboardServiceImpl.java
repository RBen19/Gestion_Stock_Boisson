package org.beni.gestionboisson.dashboard.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.dashboard.dto.DashboardStatsDTO;
import org.beni.gestionboisson.dashboard.dto.LotAnalyticsDTO;
import org.beni.gestionboisson.dashboard.dto.MouvementAnalyticsDTO;
import org.beni.gestionboisson.dashboard.repository.DashboardRepository;
import org.beni.gestionboisson.dashboard.service.DashboardService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class DashboardServiceImpl implements DashboardService {

    @Inject
    private DashboardRepository dashboardRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalUtilisateurs = dashboardRepository.countUtilisateurs();
        long totalRoles = dashboardRepository.countRoles();
        long totalBoissons = dashboardRepository.countBoissons();
        long totalCategories = dashboardRepository.countCategories();
        long totalUnitesDeMesure = dashboardRepository.countUnitesDeMesure();
        long totalFournisseurs = dashboardRepository.countFournisseurs();
        long totalEmplacements = dashboardRepository.countEmplacements();
        long totalTypeMouvements = dashboardRepository.countTypeMouvements();
        long totalLots = dashboardRepository.countLots();
        long totalTypeLotStatus = dashboardRepository.countTypeLotStatus();

        return new DashboardStatsDTO(
            totalUtilisateurs,
            totalRoles,
            totalBoissons,
            totalCategories,
            totalUnitesDeMesure,
            totalFournisseurs,
            totalEmplacements,
            totalTypeMouvements,
            totalLots,
            totalTypeLotStatus
        );
    }

     @Override
    public LotAnalyticsDTO getLotAnalytics() {
        LocalDate now = LocalDate.now();
        Instant todayStart = now.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant todayEnd = now.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant weekEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant yearStart = now.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant yearEnd = now.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        long lotsCreatedToday = dashboardRepository.countLotsCreatedBetween(todayStart, todayEnd);
        long lotsCreatedThisWeek = dashboardRepository.countLotsCreatedBetween(weekStart, weekEnd);
        long lotsCreatedThisMonth = dashboardRepository.countLotsCreatedBetween(monthStart, monthEnd);
        long lotsCreatedThisYear = dashboardRepository.countLotsCreatedBetween(yearStart, yearEnd);

        Map<String, Long> lotsByStatus = dashboardRepository.countLotsByStatus();
        Map<String, Long> lotsPerBoisson = dashboardRepository.countLotsPerBoisson();
        Double totalQuantityReceivedThisMonth = dashboardRepository.sumLotQuantityReceivedBetween(monthStart, monthEnd);
        Map<String, Long> quantityAvailablePerBoisson = dashboardRepository.getQuantityAvailablePerBoisson();

        return new LotAnalyticsDTO(
                lotsCreatedToday,
                lotsCreatedThisWeek,
                lotsCreatedThisMonth,
                lotsCreatedThisYear,
                lotsByStatus,
                lotsPerBoisson,
                totalQuantityReceivedThisMonth,
                quantityAvailablePerBoisson
        );
    }

    @Override
    public MouvementAnalyticsDTO getMouvementAnalytics() {
        LocalDate now = LocalDate.now();
        Instant todayStart = now.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant todayEnd = now.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant weekEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant yearStart = now.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant yearEnd = now.with(TemporalAdjusters.lastDayOfYear()).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        long mouvementsCreatedToday = dashboardRepository.countMouvementsCreatedBetween(todayStart, todayEnd);
        long mouvementsCreatedThisWeek = dashboardRepository.countMouvementsCreatedBetween(weekStart, weekEnd);
        long mouvementsCreatedThisMonth = dashboardRepository.countMouvementsCreatedBetween(monthStart, monthEnd);
        long mouvementsCreatedThisYear = dashboardRepository.countMouvementsCreatedBetween(yearStart, yearEnd);

        Map<String, Long> quantityMovedPerBoisson = dashboardRepository.countQuantityMovedPerBoisson();
        List<Object[]> top5BoissonsData = dashboardRepository.findTop5MostMovedBoissons();
        Map<String, Double> top5MostMovedBoissons = top5BoissonsData.stream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (double) ((Number) row[1]).longValue()));

        BigDecimal totalValueMoved = dashboardRepository.sumTotalValueMoved();

        return new MouvementAnalyticsDTO(
                mouvementsCreatedToday,
                mouvementsCreatedThisWeek,
                mouvementsCreatedThisMonth,
                mouvementsCreatedThisYear,
                quantityMovedPerBoisson,
                top5MostMovedBoissons,
                totalValueMoved
        );
    }

}
