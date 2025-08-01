package org.beni.gestionboisson.mouvement.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.mouvement.entities.Mouvement;
import org.beni.gestionboisson.mouvement.repository.MouvementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MouvementRepositoryImpl implements MouvementRepository {
    private static final Logger logger = LoggerFactory.getLogger(MouvementRepositoryImpl.class);
    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public Mouvement save(Mouvement mouvement) {
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            if (mouvement.getId() == null) {
                em.persist(mouvement);
            } else {
                mouvement = em.merge(mouvement);
            }
            tx.commit();
            return mouvement;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            logger.error("Erreur lors de l'enregistrement du mouvement", e);
            throw new RuntimeException("Erreur lors de l'enregistrement du mouvement", e);
        }

    }

    @Override
    public Optional<Mouvement> findById(Long id) {
        try {
            return Optional.of(em.createQuery(
                "SELECT m FROM Mouvement m " +
                "LEFT JOIN FETCH m.typeMouvement " +
                "LEFT JOIN FETCH m.utilisateur u " +
                "LEFT JOIN FETCH u.role " +
                "WHERE m.id = :id", Mouvement.class)
                .setParameter("id", id)
                .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Mouvement> findByCode(String code) {
        try {
            return Optional.of(em.createQuery(
                "SELECT m FROM Mouvement m " +
                "LEFT JOIN FETCH m.typeMouvement " +
                "LEFT JOIN FETCH m.utilisateur u " +
                "LEFT JOIN FETCH u.role " +
                "WHERE m.code = :code", Mouvement.class)
                .setParameter("code", code)
                .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mouvement> findAll() {
        return em.createQuery(
            "SELECT m FROM Mouvement m " +
            "LEFT JOIN FETCH m.typeMouvement " +
            "LEFT JOIN FETCH m.utilisateur u " +
            "LEFT JOIN FETCH u.role", Mouvement.class)
            .getResultList();
    }

    @Override
    public Integer getAllMouvementCount() {
        Long count = em.createQuery("SELECT COUNT(m) FROM Mouvement m", Long.class)
                .getSingleResult();
        return count.intValue();
    }
}
