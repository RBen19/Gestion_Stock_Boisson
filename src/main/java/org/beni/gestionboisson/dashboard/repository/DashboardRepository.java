package org.beni.gestionboisson.dashboard.repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface DashboardRepository {
    long countUtilisateurs();
    long countRoles();
    long countBoissons();
    long countCategories();
    long countUnitesDeMesure();
    long countFournisseurs();
    long countEmplacements();
    long countTypeMouvements();
    long countLots();
    long countTypeLotStatus();

    long countMouvementsCreatedBetween(Instant start, Instant end);
    Map<String, Long> countQuantityMovedPerBoisson();
    List<Object[]> findTop5MostMovedBoissons();
    BigDecimal sumTotalValueMoved();
      long countLotsCreatedBetween(Instant start, Instant end);
    Map<String, Long> countLotsByStatus();
    Map<String, Long> countLotsPerBoisson();
    Double sumLotQuantityReceivedBetween(Instant start, Instant end);
    Map<String, Long> getQuantityAvailablePerBoisson();
}
