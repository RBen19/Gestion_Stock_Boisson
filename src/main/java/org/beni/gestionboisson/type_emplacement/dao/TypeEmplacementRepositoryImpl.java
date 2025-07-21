package org.beni.gestionboisson.type_emplacement.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;
import org.beni.gestionboisson.type_emplacement.repository.TypeEmplacementRepository;

import java.util.List;
import java.util.Optional;
import com.github.slugify.Slugify;
@ApplicationScoped
public class TypeEmplacementRepositoryImpl implements TypeEmplacementRepository {

    @Inject
    private EntityManager em;

    @Override
    public Optional<TypeEmplacement> findByCode(String code) {
        return em.createQuery("SELECT t FROM TypeEmplacement t WHERE t.code = :code", TypeEmplacement.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<TypeEmplacement> findAll() {
        return em.createQuery("SELECT t FROM TypeEmplacement t", TypeEmplacement.class).getResultList();
    }

    @Override
    public TypeEmplacement save(TypeEmplacement typeEmplacement) {
        Slugify slugify = new Slugify() ;
        String slug = slugify.slugify(typeEmplacement.getLibelle());
        typeEmplacement.setCode(slug);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            if (typeEmplacement.getId() == null) {
                em.persist(typeEmplacement);
            } else {
                typeEmplacement = em.merge(typeEmplacement);
            }

            transaction.commit();
            return typeEmplacement;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur lors de l'enregistrement du type d'emplacement", e);
        }
    }

    @Override
    public Optional<TypeEmplacement> findById(Long id) {
        return Optional.ofNullable(em.find(TypeEmplacement.class, id));
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public Optional<TypeEmplacement> findByLibelle(String libelle) {
        return em.createQuery("SELECT t FROM TypeEmplacement t WHERE t.libelle = :libelle", TypeEmplacement.class)
                .setParameter("libelle", libelle)
                .getResultStream()
                .findFirst();
    }
}
