package org.beni.gestionboisson.boisson.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.repository.BoissonRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BoissonRepositoryImpl implements BoissonRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Boisson> findAll() {
        return em.createQuery("SELECT b FROM Boisson b", Boisson.class).getResultList();
    }

    @Override
    public Optional<Boisson> findById(Long id) {
        return Optional.ofNullable(em.find(Boisson.class, id));
    }

    @Override
    public Boisson save(Boisson boisson) {
        if (boisson.getId() == null) {
            em.persist(boisson);
            return boisson;
        } else {
            return em.merge(boisson);
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public long countByCategoryAndUnit(String categoryCode, String unit) {
        return em.createQuery("SELECT COUNT(b) FROM Boisson b WHERE b.categorie.codeCategorie = :categoryCode AND b.uniteDeMesure = :unit", Long.class)
                .setParameter("categoryCode", categoryCode)
                .setParameter("unit", unit)
                .getSingleResult();
    }

    @Override
    public Optional<Long> findMaxId() {
        return Optional.ofNullable(em.createQuery("SELECT MAX(b.id) FROM Boisson b", Long.class).getSingleResult());
    }
}
