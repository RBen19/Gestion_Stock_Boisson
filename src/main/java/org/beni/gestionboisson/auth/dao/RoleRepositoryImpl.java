package org.beni.gestionboisson.auth.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.repository.RoleRepository;

import java.util.Optional;

@ApplicationScoped
public class RoleRepositoryImpl implements RoleRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<Role> findByCode(String code) {
        return em.createQuery("SELECT r FROM Role r WHERE r.code = :code", Role.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional
    public Role save(Role role) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(role);
            em.flush();
            tx.commit();
            return role;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
