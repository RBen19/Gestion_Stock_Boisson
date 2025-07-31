package org.beni.gestionboisson;

import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.beni.gestionboisson.shared.database.DatabaseSeederService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/api/v1")
public class HelloApplication extends Application {
    /*
    *
    *  private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    @Inject
    private DatabaseSeederService databaseSeederService;

    public HelloApplication() {
        logger.info("🚀 HelloApplication initialisée - Gestion Boisson API v1");

        // Démarrer le seeding après un délai pour permettre aux @PostConstruct de se terminer
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Attendre 3 secondes
                if (databaseSeederService != null) {
                    logger.info("📦 Démarrage du seeding depuis HelloApplication...");
                    databaseSeederService.runAllSeeders();
                } else {
                    logger.warn("⚠️ DatabaseSeederService non disponible dans HelloApplication");
                }
            } catch (Exception e) {
                logger.error("❌ Erreur lors du seeding depuis HelloApplication: {}", e.getMessage(), e);
            }
        }).start();
    }
    *
    * */

}

