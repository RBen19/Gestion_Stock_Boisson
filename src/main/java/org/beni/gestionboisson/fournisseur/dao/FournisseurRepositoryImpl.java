package org.beni.gestionboisson.fournisseur.dao;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;
import org.beni.gestionboisson.fournisseur.repository.FournisseurRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FournisseurRepositoryImpl implements FournisseurRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<Fournisseur> findByCode(String code) {
        return em.createQuery("SELECT f FROM Fournisseur f WHERE f.codeFournisseur = :code", Fournisseur.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Fournisseur> findAll() {
        return em.createQuery("SELECT f FROM Fournisseur f", Fournisseur.class).getResultList();
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            if (fournisseur.getId() == null) {
                em.persist(fournisseur);
            } else {
                fournisseur = em.merge(fournisseur);
            }

            transaction.commit();
            return fournisseur;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de l'enregistrement du fournisseur", e);
        }
    }

    @Override
    public Optional<Fournisseur> findById(Long id) {
        return Optional.ofNullable(em.find(Fournisseur.class, id));
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
