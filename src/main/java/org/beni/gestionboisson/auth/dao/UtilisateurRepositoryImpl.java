package org.beni.gestionboisson.auth.dao;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    @Override
    public Optional<Utilisateur> findById(Long id) {
        return em.createQuery("SELECT u FROM Utilisateur u JOIN FETCH u.role WHERE u.idUtilisateur = :id", Utilisateur.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Inject
    private EntityManager em;

    @Override
    public Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur) {
        return em.createQuery("SELECT u FROM Utilisateur u JOIN FETCH u.role WHERE u.nomUtilisateur = :nomUtilisateur", Utilisateur.class)
                .setParameter("nomUtilisateur", nomUtilisateur)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return em.createQuery("SELECT u FROM Utilisateur u JOIN FETCH u.role WHERE u.email = :email", Utilisateur.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (utilisateur.getIdUtilisateur() == null) {
                em.persist(utilisateur);
            } else {
                utilisateur = em.merge(utilisateur);
            }
            tx.commit();
            return utilisateur;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e; // Propagation de l'exception
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        return em.createQuery("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.role ORDER BY u.createdAt DESC", Utilisateur.class)
                .getResultList();
    }
}
