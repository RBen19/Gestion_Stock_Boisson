package org.beni.gestionboisson.shared.database;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Import de tous les seeders avec les noms corrects
import org.beni.gestionboisson.auth.database.seeders.AuthSeeder;
import org.beni.gestionboisson.uniteDeMesure.database.seeders.UniteDeMesureSeeder;
import org.beni.gestionboisson.type_lot_status.database.seeders.TypeLotStatusSeeder;
import org.beni.gestionboisson.type_mouvement.database.seeders.TypeMouvementSeeder;
import org.beni.gestionboisson.type_emplacement.database.seeders.TypeEmplacementSeeder;
import org.beni.gestionboisson.emplacement.database.seeders.EmplacementSeeder;
import org.beni.gestionboisson.fournisseur.database.seeders.FournisseurSeeder;
import org.beni.gestionboisson.boisson.database.seeders.BoissonSeeder;
import org.beni.gestionboisson.lot.database.seeders.LotSeeder;
import org.beni.gestionboisson.mouvement.database.seeders.MouvementSeeder;

@ApplicationScoped
public class DatabaseSeederService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeederService.class);

    @Inject
    private AuthSeeder authSeeder;

    @Inject
    private UniteDeMesureSeeder uniteDeMesureSeeder;

    @Inject
    private TypeLotStatusSeeder typeLotStatusSeeder;

    @Inject
    private TypeMouvementSeeder typeMouvementSeeder;

    @Inject
    private TypeEmplacementSeeder typeEmplacementSeeder;

    @Inject
    private EmplacementSeeder emplacementSeeder;

    @Inject
    private FournisseurSeeder fournisseurSeeder;

    @Inject
    private BoissonSeeder boissonSeeder;

    @Inject
    private LotSeeder lotSeeder;

    @Inject
    private MouvementSeeder mouvementSeeder;

    /**
     * Exécute TOUS les seeders dans l'ordre optimal pour les dépendances
     * Cet ordre respecte les relations entre entités pour éviter les erreurs
     */
    public void runAllSeeders() {
        logger.info("🌱 Démarrage du processus de seeding complet de la base de données...");

        try {
            // Phase 1: Entités de base et référentiels
            logger.info("1️⃣ Seeding des rôles et utilisateurs...");
            authSeeder.init();
            
            logger.info("2️⃣ Seeding des unités de mesure...");
            uniteDeMesureSeeder.init();
            
            logger.info("3️⃣ Seeding des statuts de lot...");
            typeLotStatusSeeder.seedTypeLotStatus();
            
            logger.info("4️⃣ Seeding des types de mouvement...");
            typeMouvementSeeder.seedTypeMouvement();
            
            logger.info("5️⃣ Seeding des types d'emplacement...");
            typeEmplacementSeeder.init();
            
            logger.info("6️⃣ Seeding des emplacements...");
            emplacementSeeder.init();
            
            // Phase 2: Entités métier de base
            logger.info("7️⃣ Seeding des fournisseurs...");
            fournisseurSeeder.seedFournisseurs();
            
            logger.info("8️⃣ Seeding des boissons et catégories...");
            boissonSeeder.init();
            
            // Phase 3: Données transactionnelles pour statistiques
            logger.info("9️⃣ Seeding des lots avec données réalistes...");
            lotSeeder.seedLots();
            
            logger.info("🔟 Seeding des mouvements et lignes de mouvement...");
            mouvementSeeder.seedMouvements();

            logger.info("✅ Processus de seeding complet terminé avec succès!");
            logger.info("📊 Votre dashboard contient maintenant des données riches pour les statistiques!");

        } catch (Exception e) {
            logger.error("❌ Erreur lors du processus de seeding global: {}", e.getMessage(), e);
            throw new RuntimeException("Échec du seeding de la base de données", e);
        }
    }

    /**
     * Méthode pour réexécuter manuellement tous les seeders
     */
    public void reseedAll() {
        logger.info("🔄 Réexécution manuelle de tous les seeders...");
        
        try {
            // Ordre complet pour réexécution
            authSeeder.init();
            uniteDeMesureSeeder.init();
            typeLotStatusSeeder.seedTypeLotStatus();
            typeMouvementSeeder.seedTypeMouvement();
            typeEmplacementSeeder.init();
            emplacementSeeder.init();
            fournisseurSeeder.seedFournisseurs();
            boissonSeeder.init();
            lotSeeder.seedLots();
            mouvementSeeder.seedMouvements();
            
            logger.info("✅ Réexécution complète des seeders terminée!");
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la réexécution des seeders: {}", e.getMessage(), e);
        }
    }
}