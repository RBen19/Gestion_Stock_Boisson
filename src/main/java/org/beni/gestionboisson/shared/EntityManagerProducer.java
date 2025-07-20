package org.beni.gestionboisson.shared;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion_boissonPU");

    @Produces
    @RequestScoped // Un EntityManager par requête HTTP
    public EntityManager produceEntityManager() {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

    public void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
