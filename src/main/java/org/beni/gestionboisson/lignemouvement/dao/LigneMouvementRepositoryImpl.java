package org.beni.gestionboisson.lignemouvement.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.lignemouvement.entities.LigneMouvement;
import org.beni.gestionboisson.lignemouvement.repository.LigneMouvementRepository;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LigneMouvementRepositoryImpl implements LigneMouvementRepository {

    @Inject
    private EntityManager em;

    @Override
    public LigneMouvement save(LigneMouvement ligneMouvement) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (ligneMouvement.getId() == null) {
                em.persist(ligneMouvement);
            } else {
                ligneMouvement = em.merge(ligneMouvement);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return ligneMouvement;

    }

    @Override
    public Optional<LigneMouvement> findById(Long id) {
        return Optional.ofNullable(em.find(LigneMouvement.class, id));
    }

    @Override
    public Optional<LigneMouvement> findByCode(String code) {
        try {
            return Optional.of(em.createQuery("SELECT lm FROM LigneMouvement lm WHERE lm.code = :code", LigneMouvement.class)
                    .setParameter("code", code)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<LigneMouvement> findAll() {
        return em.createQuery("SELECT lm FROM LigneMouvement lm", LigneMouvement.class).getResultList();
    }

    @Override
    public List<LigneMouvement> findByMouvementId(Long mouvementId) {
        return em.createQuery("SELECT lm FROM LigneMouvement lm WHERE lm.mouvement.id = :mouvementId", LigneMouvement.class)
                .setParameter("mouvementId", mouvementId)
                .getResultList();
    }
}
