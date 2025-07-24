package org.beni.gestionboisson.lot.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.repository.LotRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LotRepositoryImpl implements LotRepository {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LotRepositoryImpl.class);

    @Inject
    private EntityManager em;

    @Override
    public Optional<Lot> findById(Long id) {
        return Optional.ofNullable(em.find(Lot.class, id));
    }

    @Override
    public Optional<Lot> findByNumeroLot(String numeroLot) {
        try {
            return Optional.of(em.createQuery("SELECT l FROM Lot l WHERE l.numeroLot = :numeroLot", Lot.class)
                    .setParameter("numeroLot", numeroLot)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lot> findAll() {
        return em.createQuery("SELECT l FROM Lot l", Lot.class).getResultList();
    }

    @Override
    public Lot save(Lot lot) {
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (lot.getId() == null) {
                em.persist(lot);
                logger.info("Persisting new Lot for boisson: {}", lot.getBoisson().getNom());
            } else {
                lot = em.merge(lot);
                logger.info("Merging existing Lot with ID: {}", lot.getId());
            }

            transaction.commit();
            logger.info("Lot saved successfully with ID: {}", lot.getId());
            return lot;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("error: {}", e.getMessage());
            logger.error("Error saving Lot for boisson:{}", lot.getBoisson().getNom()+ e);
            throw new RuntimeException("Failed to save Lot", e);
        }
    }

    @Override
    public void delete(Long id) {
        em.remove(em.find(Lot.class, id));
    }

    @Override
    public List<Lot> findAvailableLotsByBoissonCode(String boissonCode) {
        TypedQuery<Lot> query = em.createQuery(
                "SELECT l FROM Lot l WHERE l.boisson.codeBoisson = :boissonCode AND l.quantiteActuelle > 0 AND l.typeLotStatus.libelle = 'Actif'", Lot.class);
        query.setParameter("boissonCode", boissonCode);
        return query.getResultList();
    }
}
