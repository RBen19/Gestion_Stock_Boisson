package org.beni.gestionboisson.emplacement.database.seeders;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.service.EmplacementService;

@ApplicationScoped
public class EmplacementSeeder {

    @Inject
    EmplacementService emplacementService;

    @PostConstruct
    public void init() {
        seedEmplacements();
    }

    private void seedEmplacements() {
        if (emplacementService.getAllEmplacements().isEmpty()) {
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Rayon A").codeTypeEmplacement("DISPLAY").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Frigo 1").codeTypeEmplacement("REFRIGERATOR").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Zone Réception").codeTypeEmplacement("RECEIVING").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Stock Principal").codeTypeEmplacement("STOCK").build());
            emplacementService.createEmplacement(EmplacementDTO.builder().nom("Quai Expédition").codeTypeEmplacement("SHIPPING").build());
            System.out.println("Emplacements seeded.");
        }
    }
}
