package org.beni.gestionboisson.boisson.database.seeders;

import jakarta.annotation.PostConstruct;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.BoissonService;
import org.beni.gestionboisson.boisson.service.CategorieService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class BoissonSeeder {

    private static final Logger LOGGER = Logger.getLogger(BoissonSeeder.class.getName());

    @Inject
    private BoissonService boissonService;

    @Inject
    private CategorieService categorieService;

    @Inject
    private CategorieRepository categorieRepository;

    @PostConstruct
    public void init() {
        seedBoissons();
    }

    private void seedBoissons() {
        LOGGER.info("Démarrage du seeder de boissons...");

        // Seed some example boissons
        List<BoissonDTO> boissonsToSeed = List.of(
                BoissonDTO.builder().nom("Eau Plate").uniteDeMesure("L").codeCategorie("EAU_PLATE").build(),
                BoissonDTO.builder().nom("Eau Gazeuse").uniteDeMesure("L").codeCategorie("EAU_GAZEUSE").build(),
                BoissonDTO.builder().nom("Eau Minérale Cristaline").uniteDeMesure("L").codeCategorie("EAU_MINERALE").build(),
                BoissonDTO.builder().nom("Eau Citron Menthe").uniteDeMesure("L").codeCategorie("EAU_AROMATISEE").build(),

                BoissonDTO.builder().nom("Coca-Cola").uniteDeMesure("33CL").codeCategorie("SODAS").build(),
                BoissonDTO.builder().nom("Fanta Orange").uniteDeMesure("33CL").codeCategorie("SODAS").build(),
                BoissonDTO.builder().nom("Limonade Artisanale").uniteDeMesure("33CL").codeCategorie("LIMONADES").build(),
                BoissonDTO.builder().nom("Tonic Schweppes").uniteDeMesure("25CL").codeCategorie("TONICS").build(),
                BoissonDTO.builder().nom("Lipton Ice Tea Pêche").uniteDeMesure("33CL").codeCategorie("THES_GLACES").build(),
                BoissonDTO.builder().nom("Red Bull").uniteDeMesure("25CL").codeCategorie("BOISSONS_ENERGISANTES").build(),

                BoissonDTO.builder().nom("Jus d'Orange UHT").uniteDeMesure("1L").codeCategorie("JUS_FRUITS_100").build(),
                BoissonDTO.builder().nom("Nectar de Mangue").uniteDeMesure("1L").codeCategorie("NECTARS_FRUITS").build(),

                BoissonDTO.builder().nom("Lait Entier UHT").uniteDeMesure("1L").codeCategorie("LAIT_NATURE").build(),
                BoissonDTO.builder().nom("Lait Chocolat UHT").uniteDeMesure("25CL").codeCategorie("LAITS_AROMATISES").build(),
                BoissonDTO.builder().nom("Lait d'Amande UHT").uniteDeMesure("1L").codeCategorie("BOISSONS_VEGETALES").build(),

                BoissonDTO.builder().nom("Café Expresso en Brique").uniteDeMesure("25CL").codeCategorie("CAFE").build(),
                BoissonDTO.builder().nom("Thé Noir Infusé").uniteDeMesure("25CL").codeCategorie("THE").build(),

                BoissonDTO.builder().nom("Bière Blonde").uniteDeMesure("33CL").codeCategorie("BIERE_BLONDE").build(),
                BoissonDTO.builder().nom("Bière Ambrée").uniteDeMesure("33CL").codeCategorie("BIERE_AMBREE").build(),

                BoissonDTO.builder().nom("Vin Rouge Bordeaux").uniteDeMesure("75CL").codeCategorie("VIN_ROUGE").build(),
                BoissonDTO.builder().nom("Vin Rosé de Provence").uniteDeMesure("75CL").codeCategorie("VIN_ROSE").build(),

                BoissonDTO.builder().nom("Whisky Single Malt").uniteDeMesure("70CL").codeCategorie("WHISKY").build(),
                BoissonDTO.builder().nom("Vodka Absolut").uniteDeMesure("70CL").codeCategorie("VODKA").build(),

                BoissonDTO.builder().nom("Pastis de Marseille").uniteDeMesure("1L").codeCategorie("PASTIS_ANISE").build(),
                BoissonDTO.builder().nom("Grand Marnier").uniteDeMesure("70CL").codeCategorie("LIQUEURS").build()

                );

        for (BoissonDTO boissonDTO : boissonsToSeed) {
            try {
                Optional<Categorie> categorie = categorieRepository.findByCode(boissonDTO.getCodeCategorie());
                if (categorie.isPresent()) {
                 //   boissonDTO.setIdCategorie(categorie.get().getIdCategorie());
                    boissonService.createBoisson(boissonDTO);
                    LOGGER.info("Boisson '" + boissonDTO.getNom() + "' semée avec succès.");
                } else {
                    LOGGER.warning("Catégorie '" + boissonDTO.getCodeCategorie() + "' non trouvée pour la boisson '" + boissonDTO.getNom() + "'. Skipping.");
                }
            } catch (Exception e) {
                LOGGER.severe("Erreur lors du semis de la boisson '" + boissonDTO.getNom() + "': " + e.getMessage());
            }
        }
        LOGGER.info("Seeder de boissons terminé.");
    }
}
