package org.beni.gestionboisson.uniteDeMesure.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.repository.UniteDeMesureRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UniteDeMesureRepositoryImpl implements UniteDeMesureRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<UniteDeMesure> findAll() {
        return em.createQuery("SELECT u FROM UniteDeMesure u", UniteDeMesure.class).getResultList();
    }

    @Override
    public Optional<UniteDeMesure> findById(Long id) {
        return Optional.ofNullable(em.find(UniteDeMesure.class, id));
    }

    @Override
    public Optional<UniteDeMesure> findByCode(String code) {
        try {
            UniteDeMesure uniteDeMesure = em.createQuery("SELECT u FROM UniteDeMesure u WHERE u.code = :code", UniteDeMesure.class)
                    .setParameter("code", code)
                    .getSingleResult();
            return Optional.of(uniteDeMesure);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UniteDeMesure save(UniteDeMesure uniteDeMesure) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (uniteDeMesure.getId() == null) {
                em.persist(uniteDeMesure);
            } else {
                uniteDeMesure = em.merge(uniteDeMesure);
            }
            transaction.commit();
            return uniteDeMesure;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving UniteDeMesure", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
