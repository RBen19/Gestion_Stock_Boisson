package org.beni.gestionboisson.boisson.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.mappers.BoissonMapper;
import org.beni.gestionboisson.boisson.repository.BoissonRepository;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.BoissonService;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BoissonServiceImpl implements BoissonService {

    @Inject
    private BoissonRepository boissonRepository;

    @Inject
    private CategorieRepository categorieRepository;

    @Override
    @Transactional
    public BoissonDTO createBoisson(BoissonDTO dto) {
        Categorie categorie = categorieRepository.findByCode(dto.getCodeCategorie())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Categorie Code"));

        long count = boissonRepository.countByCategoryAndUnit(categorie.getCodeCategorie(), dto.getUniteDeMesure());
        String codeBoisson = String.format("%s-%s-%03d", categorie.getCodeCategorie().substring(0, 3), dto.getUniteDeMesure(), count + 1);

        Long maxId = boissonRepository.findMaxId().orElse(0L);
      //  String codeBoisson = String.format("%s-%d--%s", categorie.getCodeCategorie().substring(0, 3), maxId + 1, dto.getUniteDeMesure());

        Boisson boisson = BoissonMapper.toEntity(dto);
        boisson.setCodeBoisson(codeBoisson);
        boisson.setCategorie(categorie);

        return BoissonMapper.toDTO(boissonRepository.save(boisson));
    }

    @Override
    public List<BoissonDTO> getAllBoissons() {
        return boissonRepository.findAll().stream()
                .map(BoissonMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BoissonDTO getBoissonById(Long id) {
        return boissonRepository.findById(id)
                .map(BoissonMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void seedBoissons() {
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
                createBoisson(boissonDTO);
            } catch (Exception e) {
                // Log the error, but continue seeding other boissons
                System.err.println("Error seeding boisson: " + boissonDTO.getNom() + " - " + e.getMessage());
            }
        }
    }

    @Override
    public BoissonDTO getBoissonByCode(String codeBoisson) {
      return boissonRepository.getBoissonByCode(codeBoisson)
              .map(BoissonMapper::toDTO).orElse(null);

    }
}
