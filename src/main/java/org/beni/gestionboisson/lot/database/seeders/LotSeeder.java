package org.beni.gestionboisson.lot.database.seeders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.repository.BoissonRepository;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;
import org.beni.gestionboisson.fournisseur.repository.FournisseurRepository;
import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.service.LotService;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;
import org.beni.gestionboisson.type_lot_status.repository.TypeLotStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class LotSeeder {

    private static final Logger logger = LoggerFactory.getLogger(LotSeeder.class);

    @Inject
    private LotService lotService;

    @Inject
    private BoissonRepository boissonRepository;

    @Inject
    private FournisseurRepository fournisseurRepository;

    @Inject
    private TypeLotStatusRepository typeLotStatusRepository;

    @Inject
    private EmplacementRepository emplacementRepository;

    private final Random random = new Random();

    public void seedLots() {
        logger.info("📦 Début du seeding des lots...");

        try {
            // Vérifier si des lots existent déjà
            if (!lotService.getAllLots().isEmpty()) {
                logger.info("⚠️ Des lots existent déjà, seeding ignoré");
                return;
            }

            // Récupérer les données de référence
            List<Boisson> boissons = boissonRepository.findAll();
            List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
            List<TypeLotStatus> statusList = typeLotStatusRepository.findAll();
            List<Emplacement> emplacements = emplacementRepository.findAll();

            if (boissons.isEmpty() || fournisseurs.isEmpty() || statusList.isEmpty() || emplacements.isEmpty()) {
                logger.warn("⚠️ Données de référence manquantes pour créer les lots");
                return;
            }

            TypeLotStatus statusActif = statusList.stream()
                    .filter(status -> "actif".equals(status.getSlug()))
                    .findFirst()
                    .orElse(statusList.get(0));

            Emplacement emplacementReception = emplacements.stream()
                    .filter(emp -> emp.getCodeEmplacement().contains("ZR") || 
                                  emp.getNom().toLowerCase().contains("réception"))
                    .findFirst()
                    .orElse(emplacements.get(0));

            // Créer des lots variés pour statistiques intéressantes
            createLotsForStatistics(boissons, fournisseurs, statusActif, emplacementReception);

            logger.info("✅ Seeding des lots terminé avec succès");

        } catch (Exception e) {
            logger.error("❌ Erreur lors du seeding des lots: {}", e.getMessage(), e);
            throw new RuntimeException("Échec du seeding des lots", e);
        }
    }

    private void createLotsForStatistics(List<Boisson> boissons, List<Fournisseur> fournisseurs, 
                                       TypeLotStatus statusActif, Emplacement emplacement) {
        
        // Créer 30-50 lots pour des statistiques intéressantes
        int nombreLots = 35 + random.nextInt(15);
        
        for (int i = 0; i < nombreLots; i++) {
            try {
                Boisson boisson = boissons.get(random.nextInt(boissons.size()));
                Fournisseur fournisseur = fournisseurs.get(random.nextInt(fournisseurs.size()));
                
                // Dates variées pour l'analyse temporelle
                LocalDate dateAcquisition = generateRandomAcquisitionDate();
                LocalDate dateLimite = dateAcquisition.plusDays(30 + random.nextInt(335)); // 1 mois à 1 an
                
                // Quantités réalistes
                Double quantiteInitiale = generateRealisticQuantity(boisson);
                Double quantiteActuelle = generateCurrentQuantity(quantiteInitiale);

                LotDTO lotDTO = LotDTO.builder()
                        .boissonCode(boisson.getCodeBoisson())
                        .fournisseurCode(fournisseur.getCodeFournisseur())
                        .typeLotStatusCode(statusActif.getSlug())
                        .dateAcquisition(dateAcquisition)
                        .dateLimiteConsommation(dateLimite)
                        .quantiteInitiale(quantiteInitiale)
                        .quantiteActuelle(quantiteActuelle)
                        .codeEmplacementActuel(emplacement.getCodeEmplacement())
                        .codeEmplacementDestination(emplacement.getCodeEmplacement())
                        .utilisateurEmail("rben19@example.com")
                        .notes("Lot créé par seeder - " + boisson.getNom())
                        .build();

                lotService.createLot(lotDTO);
                
                if ((i + 1) % 10 == 0) {
                    logger.info("✓ {} lots créés...", i + 1);
                }

            } catch (Exception e) {
                logger.warn("⚠️ Erreur création lot {}: {}", i + 1, e.getMessage());
            }
        }
    }

    private LocalDate generateRandomAcquisitionDate() {
        // Dates entre il y a 6 mois et aujourd'hui
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(6);
        long daysBetween = startDate.until(today).getDays();
        return startDate.plusDays(random.nextInt((int) daysBetween + 1));
    }

    private Double generateRealisticQuantity(Boisson boisson) {
        // Quantités selon le type de boisson
        String nom = boisson.getNom().toLowerCase();
        
        if (nom.contains("eau") || nom.contains("water")) {
            return 500.0 + random.nextDouble() * 1500; // 500-2000L
        } else if (nom.contains("jus") || nom.contains("juice")) {
            return 100.0 + random.nextDouble() * 400; // 100-500L
        } else if (nom.contains("soda") || nom.contains("coca") || nom.contains("pepsi")) {
            return 200.0 + random.nextDouble() * 800; // 200-1000L
        } else if (nom.contains("bière") || nom.contains("beer")) {
            return 150.0 + random.nextDouble() * 350; // 150-500L
        } else {
            return 50.0 + random.nextDouble() * 450; // 50-500L pour autres
        }
    }

    private Double generateCurrentQuantity(Double quantiteInitiale) {
        // Entre 30% et 100% de la quantité initiale
        double pourcentage = 0.3 + random.nextDouble() * 0.7;
        return Math.round(quantiteInitiale * pourcentage * 100.0) / 100.0;
    }
}