package org.beni.gestionboisson.type_mouvement.database.seeders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;
import org.beni.gestionboisson.type_mouvement.service.TypeMouvementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TypeMouvementSeeder {

    private static final Logger logger = LoggerFactory.getLogger(TypeMouvementSeeder.class);

    @Inject
    private TypeMouvementService typeMouvementService;

    public void seedTypeMouvement() {
        logger.info("Début du seeding des types de mouvement...");

        try {
            // Vérifier si des données existent déjà
            if (!typeMouvementService.getAllTypeMouvements().isEmpty()) {
                logger.info("TypeMouvement déjà présents, seeding ignoré");
                return;
            }

            // Types de mouvements essentiels
            createTypeMouvementIfNotExists("Réception", "RECEPTION");
            createTypeMouvementIfNotExists("Transfert", "TRANSFERT");
            createTypeMouvementIfNotExists("Transfert Multiple", "TRANSFERT_MULTIPLE");
            createTypeMouvementIfNotExists("Sortie", "SORTIE");
            createTypeMouvementIfNotExists("Sortie Échantillon", "SORTIE_ECHANTILLON");
            createTypeMouvementIfNotExists("Retour Fournisseur", "RETOUR_FOURNISSEUR");
            createTypeMouvementIfNotExists("Destruction", "DESTRUCTION");
            createTypeMouvementIfNotExists("Inventaire", "INVENTAIRE");
            createTypeMouvementIfNotExists("Ajustement", "AJUSTEMENT");

            logger.info("Seeding des TypeMouvement terminé avec succès");

        } catch (Exception e) {
            logger.error("Erreur lors du seeding des TypeMouvement: {}", e.getMessage(), e);
            throw new RuntimeException("Échec du seeding TypeMouvement", e);
        }
    }

    private void createTypeMouvementIfNotExists(String libelle, String code) {
        try {
            TypeMouvementDTO dto = TypeMouvementDTO.builder()
                    .libelle(libelle)
                    .code(code)
                    .build();
            
            typeMouvementService.createTypeMouvement(dto);
            logger.info("TypeMouvement créé: {} ({})", libelle, code);
            
        } catch (Exception e) {
            logger.warn("TypeMouvement '{}' existe peut-être déjà: {}", libelle, e.getMessage());
        }
    }
}