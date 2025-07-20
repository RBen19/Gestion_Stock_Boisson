package org.beni.gestionboisson.boisson.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.mappers.CategorieMapper;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.CategorieService;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategorieServiceImpl implements CategorieService {

    @Inject
    private CategorieRepository categorieRepository;

    @Override
    public List<CategorieDTO> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(CategorieMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategorieDTO getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .map(CategorieMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void seedCategories() {
        // 🧊 1. Boissons non alcoolisées (softs)
        Categorie nonAlcoolisee = createCategory("Boissons non alcoolisées", "NON_ALCOOLISEES", null);

        // A. Eaux
        Categorie eaux = createCategory("Eaux", "EAUX", nonAlcoolisee);
        createCategory("Eau plate", "EAU_PLATE", eaux);
        createCategory("Eau gazeuse", "EAU_GAZEUSE", eaux);
        createCategory("Eau minérale", "EAU_MINERALE", eaux);
        createCategory("Eau aromatisée", "EAU_AROMATISEE", eaux);

         // B. Boissons rafraîchissantes sans alcool (BRSA)
        Categorie brsa = createCategory("Boissons rafraîchissantes sans alcool", "BRSA", nonAlcoolisee);
        createCategory("Sodas", "SODAS", brsa);
        createCategory("Limonades", "LIMONADES", brsa);
        createCategory("Tonics", "TONICS", brsa);
        createCategory("Thés glacés", "THES_GLACES", brsa);
        createCategory("Boissons énergisantes", "BOISSONS_ENERGISANTES", brsa);
        createCategory("Boissons isotoniques", "BOISSONS_ISOTONIQUES", brsa);

         // C. Jus & nectars
        Categorie jusNectars = createCategory("Jus & nectars", "JUS_NECTARS", nonAlcoolisee);
        createCategory("Jus de fruits 100 %", "JUS_FRUITS_100", jusNectars);
        createCategory("Nectars de fruits", "NECTARS_FRUITS", jusNectars);

       // D. Laits et boissons végétales (UHT uniquement)
        Categorie laitsProduitsLaitiers = createCategory("Laits et produits laitiers", "LAITS_PRODUITS_LAITIERS", nonAlcoolisee);
        createCategory("Lait nature", "LAIT_NATURE", laitsProduitsLaitiers);
        createCategory("Laits aromatisés", "LAITS_AROMATISES", laitsProduitsLaitiers);
        createCategory("Boissons végétales", "BOISSONS_VEGETALES", laitsProduitsLaitiers);

        // E. Boissons chaudes (en dosettes ou poudre)
        Categorie boissonsChaudes = createCategory("Boissons chaudes", "BOISSONS_CHAUDES", nonAlcoolisee);
        createCategory("Café", "CAFE", boissonsChaudes);
        createCategory("Thé", "THE", boissonsChaudes);
        createCategory("Chocolat chaud", "CHOCOLAT_CHAUD", boissonsChaudes);

        //  2. Boissons alcoolisées
        Categorie alcoolisee = createCategory("Boissons alcoolisées", "ALCOOLISEES", null);

        // A. Bières
        Categorie bieres = createCategory("Bières", "BIERES", alcoolisee);
        createCategory("Blonde", "BIERE_BLONDE", bieres);
        createCategory("Brune", "BIERE_BRUNE", bieres);
        createCategory("Ambrée", "BIERE_AMBREE", bieres);
        createCategory("Blanche", "BIERE_BLANCHE", bieres);
        createCategory("IPA, Stout, Lager, etc.", "BIERE_AUTRES", bieres);

        // B. Vins
        Categorie vins = createCategory("Vins", "VINS", alcoolisee);
        createCategory("Rouge", "VIN_ROUGE", vins);
        createCategory("Blanc", "VIN_BLANC", vins);
        createCategory("Rosé", "VIN_ROSE", vins);
        createCategory("Vin pétillant", "VIN_PETILLANT", vins);

        // C. Spiritueux
        Categorie spiritueux = createCategory("Spiritueux", "SPIRITUEUX", alcoolisee);
        createCategory("Whisky", "WHISKY", spiritueux);
        createCategory("Rhum", "RHUM", spiritueux);
        createCategory("Vodka", "VODKA", spiritueux);
        createCategory("Tequila", "TEQUILA", spiritueux);
        createCategory("Gin", "GIN", spiritueux);
        createCategory("Cognac, Armagnac", "COGNAC_ARMAGNAC", spiritueux);

        // D. Apéritifs
        Categorie aperitifs = createCategory("Apéritifs", "APERITIFS", alcoolisee);
        createCategory("Vermouth", "VERMOUTH", aperitifs);
        createCategory("Pastis / Anisé", "PASTIS_ANISE", aperitifs);
        createCategory("Liqueurs", "LIQUEURS", aperitifs);
        createCategory("Amers, bitters", "AMERS_BITTERS", aperitifs);


    }

    private Categorie createCategory(String name, String code, Categorie parent) {
        return categorieRepository.findByCode(code).orElseGet(() -> {
            Categorie categorie = new Categorie();
            categorie.setNom(name);
            categorie.setCodeCategorie(code);
            categorie.setParentCategorie(parent);
            return categorieRepository.save(categorie);
        });
    }
}
