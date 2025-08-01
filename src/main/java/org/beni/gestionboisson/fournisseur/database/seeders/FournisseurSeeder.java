package org.beni.gestionboisson.fournisseur.database.seeders;

import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.service.FournisseurService;

import java.util.logging.Logger;

@ApplicationScoped
public class FournisseurSeeder {

    private static final Logger LOGGER = Logger.getLogger(FournisseurSeeder.class.getName());

    @Inject
    private FournisseurService fournisseurService;

    //@PostConstruct
    public void init() {
        seedFournisseurs();
    }

    public void seedFournisseurs() {
        LOGGER.info("Démarrage du seeder de fournisseurs...");

        try {
            // Vérifier si des fournisseurs existent déjà
            if (!fournisseurService.getAllFournisseurs().isEmpty()) {
                LOGGER.info("Fournisseurs déjà présents, seeding ignoré");
                return;
            }

            CreateFournisseurDTO fournisseur1 = new CreateFournisseurDTO();
            fournisseur1.setNom("Coca-Cola");
            fournisseur1.setEmailContact("contact@coca-cola.com");
            fournisseur1.setNumeroTelephone("123456789");
            fournisseurService.createFournisseur(fournisseur1);

            CreateFournisseurDTO fournisseur2 = new CreateFournisseurDTO();
            fournisseur2.setNom("Fanta");
            fournisseur2.setEmailContact("contact@fanta.com");
            fournisseur2.setNumeroTelephone("987654321");
            fournisseurService.createFournisseur(fournisseur2);

            LOGGER.info("Seeder de fournisseurs terminé.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors du seeding des fournisseurs: " + e.getMessage());
            throw new RuntimeException("Échec du seeding des fournisseurs", e);
        }
    }
}
