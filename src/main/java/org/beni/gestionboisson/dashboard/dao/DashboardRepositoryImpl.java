package org.beni.gestionboisson.dashboard.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.dashboard.repository.DashboardRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class DashboardRepositoryImpl implements DashboardRepository {

   @Inject
    private EntityManager em;

    @Override
    public long countUtilisateurs() {
        return (long) em.createQuery("SELECT COUNT(u) FROM Utilisateur u").getSingleResult();
    }

    @Override
    public long countRoles() {
        return (long) em.createQuery("SELECT COUNT(r) FROM Role r").getSingleResult();
    }

    @Override
    public long countBoissons() {
        return (long) em.createQuery("SELECT COUNT(b) FROM Boisson b").getSingleResult();
    }

    @Override
    public long countCategories() {
        return (long) em.createQuery("SELECT COUNT(c) FROM Categorie c").getSingleResult();
    }

    @Override
    public long countUnitesDeMesure() {
        return (long) em.createQuery("SELECT COUNT(u) FROM UniteDeMesure u").getSingleResult();
    }

    @Override
    public long countFournisseurs() {
        return (long) em.createQuery("SELECT COUNT(f) FROM Fournisseur f").getSingleResult();
    }

    @Override
    public long countEmplacements() {
        return (long) em.createQuery("SELECT COUNT(e) FROM Emplacement e").getSingleResult();
    }

    @Override
    public long countTypeMouvements() {
        return (long) em.createQuery("SELECT COUNT(t) FROM TypeMouvement t").getSingleResult();
    }

    @Override
    public long countLots() {
        return (long) em.createQuery("SELECT COUNT(l) FROM Lot l").getSingleResult();
    }

    @Override
    public long countTypeLotStatus() {
        return (long) em.createQuery("SELECT COUNT(t) FROM TypeLotStatus t").getSingleResult();
    }
       @Override
    public long countLotsCreatedBetween(Instant start, Instant end) {
        return (long) em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public Map<String, Long> countLotsByStatus() {
        return em.createQuery("SELECT tls.slug, COUNT(l) FROM Lot l JOIN l.typeLotStatus tls GROUP BY tls.slug", Object[].class)
                .getResultStream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (Long) row[1]));
    }

    @Override
    public Map<String, Long> countLotsPerBoisson() {
        return em.createQuery("SELECT b.nom, COUNT(l) FROM Lot l JOIN l.boisson b GROUP BY b.nom", Object[].class)
                .getResultStream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> (Long) row[1]));
    }

    @Override
    public Double sumLotQuantityReceivedBetween(Instant start, Instant end) {
        Object result = em.createQuery("SELECT SUM(l.quantiteInitiale) FROM Lot l WHERE l.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
        return  result.equals(0) ? 0.0 : (Double) result;
    }

    @Override
    public Map<String, Long> getQuantityAvailablePerBoisson() {
        return em.createQuery("SELECT b.nom, SUM(l.quantiteActuelle) FROM Lot l JOIN l.boisson b GROUP BY b.nom", Object[].class)
                .getResultStream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> ((Number) row[1]).longValue()));
    }
    @Override
    public long countMouvementsCreatedBetween(Instant start, Instant end) {
        return (long) em.createQuery("SELECT COUNT(m) FROM Mouvement m WHERE m.createdAt BETWEEN :start AND :end")
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
    }

    @Override
    public Map<String, Long> countQuantityMovedPerBoisson() {
        return em.createQuery("SELECT b.nom, SUM(lm.quantite) FROM LigneMouvement lm JOIN lm.lot l JOIN l.boisson b GROUP BY b.nom", Object[].class)
                .getResultStream()
                .collect(Collectors.toMap(row -> (String) row[0], row -> ((Number) row[1]).longValue()));
    }

    @Override
    public List<Object[]> findTop5MostMovedBoissons() {
        return em.createQuery("SELECT b.nom, SUM(lm.quantite) AS total_quantite FROM LigneMouvement lm JOIN lm.lot l JOIN l.boisson b GROUP BY b.nom ORDER BY total_quantite DESC", Object[].class)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public BigDecimal sumTotalValueMoved() {
        return (BigDecimal) em.createQuery("SELECT SUM(lm.montantTotal) FROM LigneMouvement lm").getSingleResult();
    }


}
