package org.beni.gestionboisson.boisson.database.seeders;

import jakarta.annotation.PostConstruct;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.BoissonService;
import org.beni.gestionboisson.boisson.service.CategorieService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@ApplicationScoped
public class BoissonSeeder {

   private static final Logger LOGGER = Logger.getLogger(BoissonSeeder.class.getName());

    @Inject
    private BoissonService boissonService;

    @Inject
    private CategorieService categorieService;

    @Inject
    private CategorieRepository categorieRepository;

   // @PostConstruct
    public void init() {
        boissonService.seedBoissons();
    }

    public void seedBoissons() {
        LOGGER.info("Démarrage du seeder de boissons...");
        // The actual seeding logic is now in BoissonServiceImpl
        LOGGER.info("Seeder de boissons terminé.");
    }
}
