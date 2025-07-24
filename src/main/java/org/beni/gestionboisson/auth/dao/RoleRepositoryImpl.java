package org.beni.gestionboisson.auth.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.repository.RoleRepository;

import java.util.List;
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

    @Override
    public List<Role> findAll() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public Role getRoleById(Long id) {
        return em.find(Role.class, id);
    }

    @Override
    public Role createRole(Role role) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
        //    role.setStatus(true);
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

    @Override
    public Role updateRole(Long id, Role role) {
        EntityTransaction tx = em.getTransaction();
        Role roleToUpdate = em.find(Role.class, id);
        try {
            tx.begin();
            em.merge(roleToUpdate);
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

    @Override
    public void deleteRole(Long id) {
        em.find(Role.class, id);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Role role = em.find(Role.class, id);
            role.setStatus(false);
            em.merge(role);
            em.flush();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(em.find(Role.class, id));
    }


}
