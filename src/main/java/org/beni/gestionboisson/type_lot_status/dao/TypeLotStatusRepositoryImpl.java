package org.beni.gestionboisson.type_lot_status.dao;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;
import org.beni.gestionboisson.type_lot_status.repository.TypeLotStatusRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TypeLotStatusRepositoryImpl implements TypeLotStatusRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<TypeLotStatus> findById(Long id) {
        return Optional.ofNullable(em.find(TypeLotStatus.class, id));
    }

    @Override
    public Optional<TypeLotStatus> findByLibelle(String libelle) {
        try {
            return Optional.of(em.createQuery("SELECT tls FROM TypeLotStatus tls WHERE tls.libelle = :libelle", TypeLotStatus.class)
                    .setParameter("libelle", libelle)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TypeLotStatus> findBySlug(String slug) {
        try {
            return Optional.of(em.createQuery("SELECT tls FROM TypeLotStatus tls WHERE tls.slug = :slug", TypeLotStatus.class)
                    .setParameter("slug", slug)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TypeLotStatus> findAll() {
        return em.createQuery("SELECT tls FROM TypeLotStatus tls", TypeLotStatus.class).getResultList();
    }

    @Override
    public TypeLotStatus save(TypeLotStatus typeLotStatus) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (typeLotStatus.getId() == null) {
                em.persist(typeLotStatus);
            } else {
                typeLotStatus = em.merge(typeLotStatus);
            }
            transaction.commit();
            return typeLotStatus;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving TypeLotStatus", e);
        }
    }

    @Override
    public void delete(TypeLotStatus typeLotStatus) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(typeLotStatus) ? typeLotStatus : em.merge(typeLotStatus));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting TypeLotStatus", e);
        }
    }
}
