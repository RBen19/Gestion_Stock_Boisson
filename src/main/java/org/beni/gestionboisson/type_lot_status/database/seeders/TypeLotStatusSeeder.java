package org.beni.gestionboisson.type_lot_status.database.seeders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;
import org.beni.gestionboisson.type_lot_status.service.TypeLotStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TypeLotStatusSeeder {

    private static final Logger logger = LoggerFactory.getLogger(TypeLotStatusSeeder.class);

    @Inject
    private TypeLotStatusService typeLotStatusService;

    public void seedTypeLotStatus() {
        logger.info("🏷️ Début du seeding des types de statut de lot...");

        try {
            // Vérifier si des données existent déjà
            if (!typeLotStatusService.getAllTypeLotStatuses().isEmpty()) {
                logger.info("⚠️ TypeLotStatus déjà présents, seeding ignoré");
                return;
            }

            // Statuts essentiels pour la gestion des lots
            createTypeLotStatusIfNotExists("Actif", "actif");
            createTypeLotStatusIfNotExists("Inactif", "inactif");
            createTypeLotStatusIfNotExists("Expiré", "expire");
            createTypeLotStatusIfNotExists("En quarantaine", "quarantaine");
            createTypeLotStatusIfNotExists("Réservé", "reserve");
            createTypeLotStatusIfNotExists("Endommagé", "endommage");
            createTypeLotStatusIfNotExists("Retour fournisseur", "retour_fournisseur");

            logger.info("✅ Seeding des TypeLotStatus terminé avec succès");

        } catch (Exception e) {
            logger.error("❌ Erreur lors du seeding des TypeLotStatus: {}", e.getMessage(), e);
            throw new RuntimeException("Échec du seeding TypeLotStatus", e);
        }
    }

    private void createTypeLotStatusIfNotExists(String libelle, String slug) {
        try {
            TypeLotStatusDTO dto = TypeLotStatusDTO.builder()
                    .libelle(libelle)
                    .slug(slug)
                    .build();
            
            typeLotStatusService.createTypeLotStatus(dto);
            logger.info("✓ TypeLotStatus créé: {} ({})", libelle, slug);
            
        } catch (Exception e) {
            logger.warn("⚠️ TypeLotStatus '{}' existe peut-être déjà: {}", libelle, e.getMessage());
        }
    }
}