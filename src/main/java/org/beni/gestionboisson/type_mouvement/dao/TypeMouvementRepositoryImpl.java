package org.beni.gestionboisson.type_mouvement.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;
import org.beni.gestionboisson.type_mouvement.repository.TypeMouvementRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TypeMouvementRepositoryImpl implements TypeMouvementRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<TypeMouvement> findById(Long id) {
        return Optional.ofNullable(em.find(TypeMouvement.class, id));
    }

    @Override
    public Optional<TypeMouvement> findByLibelle(String libelle) {
        try {
            return Optional.of(em.createQuery("SELECT tm FROM TypeMouvement tm WHERE tm.libelle = :libelle", TypeMouvement.class)
                    .setParameter("libelle", libelle)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TypeMouvement> findByCode(String code) {
        try {
            return Optional.of(em.createQuery("SELECT tm FROM TypeMouvement tm WHERE tm.code = :code", TypeMouvement.class)
                    .setParameter("code", code)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TypeMouvement> findAll() {
        return em.createQuery("SELECT tm FROM TypeMouvement tm", TypeMouvement.class).getResultList();
    }

    @Override
    public TypeMouvement save(TypeMouvement typeMouvement) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            TypeMouvement result;
            if (typeMouvement.getId() == null) {
                em.persist(typeMouvement);
                result = typeMouvement;
            } else {
                result = em.merge(typeMouvement);
            }

            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erreur lors de l'enregistrement du TypeMouvement", e);

        }
    }

    @Override
    @Transactional
    public void delete(TypeMouvement typeMouvement) {
        em.remove(em.contains(typeMouvement) ? typeMouvement : em.merge(typeMouvement));
    }

    @Override
    public boolean existsByLibelle(String libelle) {
        return em.createQuery("SELECT COUNT(tm) FROM TypeMouvement tm WHERE tm.libelle = :libelle", Long.class)
                .setParameter("libelle", libelle)
                .getSingleResult() > 0;
    }

    @Override
    public boolean existsByCode(String code) {
        return em.createQuery("SELECT COUNT(tm) FROM TypeMouvement tm WHERE tm.code = :code", Long.class)
                .setParameter("code", code)
                .getSingleResult() > 0;
    }
}
