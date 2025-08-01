package org.beni.gestionboisson.emplacement.database.seeders;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.service.EmplacementService;
import org.beni.gestionboisson.type_emplacement.controller.TypeEmplacementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EmplacementSeeder {

    @Inject
    EmplacementService emplacementService;
    private static final Logger logger = LoggerFactory.getLogger(EmplacementSeeder.class);



    //@PostConstruct
    public void init() {
        seedEmplacements();
    }

    private void seedEmplacements() {
        if (emplacementService.getAllEmplacements().isEmpty()) {
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Rayon A").codeTypeEmplacement("presentoir").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Frigo 1").codeTypeEmplacement("refrigerateur").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Zone Réception").codeTypeEmplacement("reception").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Stock Principal").codeTypeEmplacement("stockage").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Quai Expédition").codeTypeEmplacement("expedition").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Zone retour fournisseur").codeTypeEmplacement("retour-fournisseur").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Zone de sortie principale").codeTypeEmplacement("zone-sortie").build());
            logger.info("Emplacements seeded.");
        }
    }
}
