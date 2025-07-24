package org.beni.gestionboisson.type_emplacement.database.seeders;

import jakarta.annotation.PostConstruct;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;
import org.beni.gestionboisson.type_emplacement.service.TypeEmplacementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TypeEmplacementSeeder {

    private static final Logger logger = LoggerFactory.getLogger(TypeEmplacementSeeder.class);

    @Inject
    TypeEmplacementService typeEmplacementService;

    @PostConstruct
    public void init() {
        seedTypeEmplacements();
    }

    private void seedTypeEmplacements() {
        if (typeEmplacementService.getAllTypeEmplacements().isEmpty()) {
            typeEmplacementService.createTypeEmplacement(TypeEmplacementDTO.builder().code("STOCK").libelle("Stockage").description("Zone de stockage générale").build());
            typeEmplacementService.createTypeEmplacement(TypeEmplacementDTO.builder().code("REFRIGERATOR").libelle("Réfrigérateur").description("Zone de stockage réfrigérée").build());
            typeEmplacementService.createTypeEmplacement(TypeEmplacementDTO.builder().code("DISPLAY").libelle("Présentoir").description("Zone d'exposition des produits").build());
            typeEmplacementService.createTypeEmplacement(TypeEmplacementDTO.builder().code("RECEIVING").libelle("Réception").description("Zone de réception des marchandises").build());
            typeEmplacementService.createTypeEmplacement(TypeEmplacementDTO.builder().code("SHIPPING").libelle("Expédition").description("Zone d'expédition des marchandises").build());
            logger.info("TypeEmplacements seeded.");
        }
    }
}