package org.beni.gestionboisson.uniteDeMesure.database.seeders;

import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.uniteDeMesure.service.UniteDeMesureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UniteDeMesureSeeder {

    private static final Logger logger = LoggerFactory.getLogger(UniteDeMesureSeeder.class);

    @Inject
    private UniteDeMesureService uniteDeMesureService;

    //@PostConstruct
    public void init() {
        seedUniteDeMesure();
    }

    private void seedUniteDeMesure() {
        if (uniteDeMesureService.getAllUnitesDeMesure().isEmpty()) {
            uniteDeMesureService.seedUniteDeMesure();
            logger.info("Seeded UniteDeMesure.");
        }
    }
}
