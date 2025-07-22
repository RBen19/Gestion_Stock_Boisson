package org.beni.gestionboisson.uniteDeMesure.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.repository.UniteDeMesureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UniteDeMesureRepositoryImpl implements UniteDeMesureRepository {

    private static final Logger logger = LoggerFactory.getLogger(UniteDeMesureRepositoryImpl.class);

    @Inject
    private EntityManager em;

    @Override
    public List<UniteDeMesure> findAll() {
        logger.info("Attempting to find all UniteDeMesure entities.");
        List<UniteDeMesure> result = em.createQuery("SELECT u FROM UniteDeMesure u", UniteDeMesure.class).getResultList();
        logger.info("Found {} UniteDeMesure entities.", result.size());
        return result;
    }

    @Override
    public Optional<UniteDeMesure> findById(Long id) {
        logger.info("Attempting to find UniteDeMesure by ID: {}", id);
        Optional<UniteDeMesure> result = Optional.ofNullable(em.find(UniteDeMesure.class, id));
        if (result.isPresent()) {
            logger.info("Found UniteDeMesure with ID: {}", id);
        } else {
            logger.warn("UniteDeMesure with ID: {} not found.", id);
        }
        return result;
    }

    @Override
    public Optional<UniteDeMesure> findByCode(String code) {
        logger.info("Attempting to find UniteDeMesure by code: {}", code);
        try {
            UniteDeMesure uniteDeMesure = em.createQuery("SELECT u FROM UniteDeMesure u WHERE u.code = :code", UniteDeMesure.class)
                    .setParameter("code", code)
                    .getSingleResult();
            logger.info("Found UniteDeMesure with code: {}", code);
            return Optional.of(uniteDeMesure);
        } catch (NoResultException e) {
            logger.warn("UniteDeMesure with code: {} not found.", code);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error finding UniteDeMesure by code {}: {}", code, e.getMessage());
            throw new RuntimeException("Error finding UniteDeMesure by code", e);
        }
    }

    @Override
    public UniteDeMesure save(UniteDeMesure uniteDeMesure) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (uniteDeMesure.getId() == null) {
                em.persist(uniteDeMesure);
                logger.info("Persisting new UniteDeMesure with code: {}", uniteDeMesure.getCode());
            } else {
                uniteDeMesure = em.merge(uniteDeMesure);
                logger.info("Merging existing UniteDeMesure with ID: {}", uniteDeMesure.getId());
            }
            transaction.commit();
            logger.info("UniteDeMesure saved successfully with ID: {}", uniteDeMesure.getId());
            return uniteDeMesure;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaction rolled back due to error saving UniteDeMesure.");
            }
            logger.error("Error saving UniteDeMesure: {}", e.getMessage());
            throw new RuntimeException("Error saving UniteDeMesure", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Attempting to delete UniteDeMesure with ID: {}", id);
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            findById(id).ifPresent(uniteDeMesure -> {
                em.remove(uniteDeMesure);
                logger.info("UniteDeMesure with ID: {} removed successfully.", id);
            });
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaction rolled back due to error deleting UniteDeMesure with ID: {}", id);
            }
            logger.error("Error deleting UniteDeMesure with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting UniteDeMesure", e);
        }
    }
}
