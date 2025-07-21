package org.beni.gestionboisson.boisson.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.mappers.CategorieMapper;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.CategorieService;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
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
    public CategorieDTO createCategorie(CategorieDTO categorieDTO) {
        String baseCode = generateSlug(categorieDTO.getNom());
        String uniqueCode = generateUniqueCode(baseCode);

        Categorie categorie = CategorieMapper.toEntity(categorieDTO);
        categorie.setCodeCategorie(uniqueCode);

        if (categorieDTO.getParentCategorieId() != null) {
            Categorie parent = categorieRepository.findById(categorieDTO.getParentCategorieId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            categorie.setParentCategorie(parent);
        }

        return CategorieMapper.toDTO(categorieRepository.save(categorie));
    }

    @Override
    @Transactional
    public CategorieDTO updateCategorie(Long id, CategorieDTO categorieDTO) {
        Categorie existingCategorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingCategorie.setNom(categorieDTO.getNom());

        // Regenerate codeCategorie if name changes
        String newBaseCode = generateSlug(categorieDTO.getNom());
        if (!existingCategorie.getCodeCategorie().startsWith(newBaseCode)) {
            String uniqueCode = generateUniqueCode(newBaseCode);
            existingCategorie.setCodeCategorie(uniqueCode);
        }

        if (categorieDTO.getParentCategorieId() != null) {
            Categorie parent = categorieRepository.findById(categorieDTO.getParentCategorieId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            existingCategorie.setParentCategorie(parent);
        } else {
            existingCategorie.setParentCategorie(null);
        }

        return CategorieMapper.toDTO(categorieRepository.save(existingCategorie));
    }

    @Override
    @Transactional
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

    @Override
    public CategorieDTO getCategorieByCode(String codeCategorie) {
        return categorieRepository.findByCode(codeCategorie)
                .map(CategorieMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void seedCategories() {
        // 🧊 1. Boissons non alcoolisées (softs)
        Categorie nonAlcoolisee = createCategory("Boissons non alcoolisées", null);

        // A. Eaux
        Categorie eaux = createCategory("Eaux", nonAlcoolisee);
        createCategory("Eau plate", eaux);
        createCategory("Eau gazeuse", eaux);
        createCategory("Eau minérale", eaux);
        createCategory("Eau aromatisée", eaux);

         // B. Boissons rafraîchissantes sans alcool (BRSA)
        Categorie brsa = createCategory("Boissons rafraîchissantes sans alcool", nonAlcoolisee);
        createCategory("Sodas", brsa);
        createCategory("Limonades", brsa);
        createCategory("Tonics", brsa);
        createCategory("Thés glacés", brsa);
        createCategory("Boissons énergisantes", brsa);
        createCategory("Boissons isotoniques", brsa);

         // C. Jus & nectars
        Categorie jusNectars = createCategory("Jus & nectars", nonAlcoolisee);
        createCategory("Jus de fruits 100 %", jusNectars);
        createCategory("Nectars de fruits", jusNectars);

       // D. Laits et boissons végétales (UHT uniquement)
        Categorie laitsProduitsLaitiers = createCategory("Laits et produits laitiers", nonAlcoolisee);
        createCategory("Lait nature", laitsProduitsLaitiers);
        createCategory("Laits aromatisés", laitsProduitsLaitiers);
        createCategory("Boissons végétales", laitsProduitsLaitiers);

        // E. Boissons chaudes (en dosettes ou poudre)
        Categorie boissonsChaudes = createCategory("Boissons chaudes", nonAlcoolisee);
        createCategory("Café", boissonsChaudes);
        createCategory("Thé", boissonsChaudes);
        createCategory("Chocolat chaud", boissonsChaudes);

        //  2. Boissons alcoolisées
        Categorie alcoolisee = createCategory("Boissons alcoolisées", null);

        // A. Bières
        Categorie bieres = createCategory("Bières", alcoolisee);
        createCategory("Blonde", bieres);
        createCategory("Brune", bieres);
        createCategory("Ambrée", bieres);
        createCategory("Blanche", bieres);
        createCategory("IPA, Stout, Lager, etc.", bieres);

        // B. Vins
        Categorie vins = createCategory("Vins", alcoolisee);
        createCategory("Rouge", vins);
        createCategory("Blanc", vins);
        createCategory("Rosé", vins);
        createCategory("Vin pétillant", vins);

        // C. Spiritueux
        Categorie spiritueux = createCategory("Spiritueux", alcoolisee);
        createCategory("Whisky", spiritueux);
        createCategory("Rhum", spiritueux);
        createCategory("Vodka", spiritueux);
        createCategory("Tequila", spiritueux);
        createCategory("Gin", spiritueux);
        createCategory("Cognac, Armagnac", spiritueux);

        // D. Apéritifs
        Categorie aperitifs = createCategory("Apéritifs", alcoolisee);
        createCategory("Vermouth", aperitifs);
        createCategory("Pastis / Anisé", aperitifs);
        createCategory("Liqueurs", aperitifs);
        createCategory("Amers, bitters", aperitifs);
    }

    private Categorie createCategory(String name, Categorie parent) {
        String baseCode = generateSlug(name);
        String uniqueCode = generateUniqueCode(baseCode);

        return categorieRepository.findByCode(uniqueCode).orElseGet(() -> {
            Categorie categorie = new Categorie();
            categorie.setNom(name);
            categorie.setCodeCategorie(uniqueCode);
            categorie.setParentCategorie(parent);
            return categorieRepository.save(categorie);
        });
    }

    private String generateSlug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toUpperCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^A-Z0-9_]+", "");
    }

    private String generateUniqueCode(String baseCode) {
        String uniqueCode = baseCode;
        int counter = 0;
        while (categorieRepository.findByCode(uniqueCode).isPresent()) {
            counter++;
            uniqueCode = baseCode + "_" + counter;
        }
        return uniqueCode;
    }
}
