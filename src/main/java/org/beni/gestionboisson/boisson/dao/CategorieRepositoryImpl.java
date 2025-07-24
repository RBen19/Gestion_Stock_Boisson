package org.beni.gestionboisson.boisson.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategorieRepositoryImpl implements CategorieRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Categorie> findAll() {
        return em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
    }

    @Override
    public Optional<Categorie> findById(Long id) {
        return Optional.ofNullable(em.find(Categorie.class, id));
    }

    @Override
    @Transactional
    public Categorie save(Categorie categorie) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (categorie.getIdCategorie() == null) {
                em.persist(categorie);
                em.flush();
            } else {
                categorie = em.merge(categorie);
            }

            tx.commit();
            return categorie;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public Optional<Categorie> findByCode(String code) {
        try {
            return Optional.of(em.createQuery("SELECT c FROM Categorie c WHERE c.codeCategorie = :code", Categorie.class)
                    .setParameter("code", code)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
