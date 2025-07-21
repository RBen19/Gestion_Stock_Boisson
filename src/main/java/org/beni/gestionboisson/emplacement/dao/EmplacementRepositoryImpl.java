package org.beni.gestionboisson.emplacement.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmplacementRepositoryImpl implements EmplacementRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<Emplacement> findByCodeEmplacement(String codeEmplacement) {
        return em.createQuery("SELECT e FROM Emplacement e WHERE e.codeEmplacement = :codeEmplacement", Emplacement.class)
                .setParameter("codeEmplacement", codeEmplacement)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Emplacement> findAll() {
        return em.createQuery("SELECT e FROM Emplacement e", Emplacement.class).getResultList();
    }

    @Override
    public Emplacement save(Emplacement emplacement) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            if (emplacement.getId() == null) {
                em.persist(emplacement);
            } else {
                emplacement = em.merge(emplacement);
            }

            transaction.commit();
            return emplacement;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de l'enregistrement de l'emplacement", e);
        }
    }

    @Override
    public Optional<Emplacement> findById(Long id) {
        return Optional.ofNullable(em.find(Emplacement.class, id));
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public Optional<Emplacement> findEmplacementByNom(String nom) {
        return em.createQuery("SELECT e FROM Emplacement e WHERE e.nom = :nom", Emplacement.class)
                .setParameter("nom", nom)
                .getResultStream()
                .findFirst();
    }
}
