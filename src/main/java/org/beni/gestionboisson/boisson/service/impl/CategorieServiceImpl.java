package org.beni.gestionboisson.boisson.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.dto.CategorieDTO;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.mappers.CategorieMapper;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.CategorieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.boisson.exceptions.CategoryNotFoundException;
import org.beni.gestionboisson.boisson.exceptions.InvalidBoissonDataException;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategorieServiceImpl implements CategorieService {

    private static final Logger logger = LoggerFactory.getLogger(CategorieServiceImpl.class);

    @Inject
    private CategorieRepository categorieRepository;

    @Override
    public List<CategorieDTO> getAllCategories() {
        logger.info("Fetching all categories.");
        return categorieRepository.findAll().stream()
                .map(CategorieMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategorieDTO getCategorieById(Long id) {
        logger.info("Fetching category by ID: {}", id);
        return categorieRepository.findById(id)
                .map(CategorieMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Category with ID {} not found.", id);
                    return new CategoryNotFoundException("Category not found with ID: " + id);
                });
    }

    @Override
    @Transactional
    public CategorieDTO createCategorie(CategorieDTO categorieDTO) {
        logger.info("Attempting to create category with name: {}", categorieDTO.getNom());
        Optional<Categorie> verifCategorie = categorieRepository.findByNom(categorieDTO.getNom());
        if (verifCategorie.isPresent()) {
            logger.error("Category with name {} already exists.", categorieDTO.getNom());
            throw new InvalidBoissonDataException("Category with name " + categorieDTO.getNom() + " already exists.");
        }
        String baseCode = generateSlug(categorieDTO.getNom());
        String uniqueCode = generateUniqueCode(baseCode);

        Categorie categorie = CategorieMapper.toEntity(categorieDTO);
        categorie.setCodeCategorie(uniqueCode);

        if (categorieDTO.getParentCategorieId() != null) {
            Categorie parent = categorieRepository.findById(categorieDTO.getParentCategorieId())
                    .orElseThrow(() -> {
                        logger.error("Parent category with ID {} not found.", categorieDTO.getParentCategorieId());
                        return new CategoryNotFoundException("Parent category not found");
                    });
            categorie.setParentCategorie(parent);
        }

        Categorie createdCategorie = categorieRepository.save(categorie);
        logger.info("Category created successfully with ID: {}", createdCategorie.getIdCategorie());
        return CategorieMapper.toDTO(createdCategorie);
    }

    @Override
    @Transactional
    public CategorieDTO updateCategorie(Long id, CategorieDTO categorieDTO) {
        logger.info("Attempting to update category with ID: {}", id);
        Categorie existingCategorie = categorieRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with ID {} not found for update.", id);
                    return new CategoryNotFoundException("Category not found");
                });

        existingCategorie.setNom(categorieDTO.getNom());

        // Regenerate codeCategorie if name changes
        String newBaseCode = generateSlug(categorieDTO.getNom());
        if (!existingCategorie.getCodeCategorie().startsWith(newBaseCode)) {
            String uniqueCode = generateUniqueCode(newBaseCode);
            existingCategorie.setCodeCategorie(uniqueCode);
        }

        if (categorieDTO.getParentCategorieId() != null) {
            Categorie parent = categorieRepository.findById(categorieDTO.getParentCategorieId())
                    .orElseThrow(() -> {
                        logger.error("Parent category with ID {} not found for update.", categorieDTO.getParentCategorieId());
                        return new CategoryNotFoundException("Parent category not found");
                    });
            existingCategorie.setParentCategorie(parent);
        } else {
            existingCategorie.setParentCategorie(null);
        }

        Categorie updatedCategorie = categorieRepository.save(existingCategorie);
        logger.info("Category with ID {} updated successfully.", updatedCategorie.getIdCategorie());
        return CategorieMapper.toDTO(updatedCategorie);
    }

    @Override
    @Transactional
    public void deleteCategorie(Long id) {
        logger.info("Attempting to delete category with ID: {}", id);
        if (!categorieRepository.findById(id).isPresent()) {
            logger.error("Category with ID {} not found for deletion.", id);
            throw new CategoryNotFoundException("Category not found with ID: " + id);
        }
        categorieRepository.deleteById(id);
        logger.info("Category with ID {} deleted successfully.", id);
    }

    @Override
    public CategorieDTO getCategorieByCode(String codeCategorie) {
        logger.info("Fetching category by code: {}", codeCategorie);
        return categorieRepository.findByCode(codeCategorie)
                .map(CategorieMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Category with code {} not found.", codeCategorie);
                    return new CategoryNotFoundException("Category not found with code: " + codeCategorie);
                });
    }

    @Override
    @Transactional
    public void seedCategories() {
        logger.info("Starting category seeding process.");
        logger.info("fetch all category ...");
       if (!categorieRepository.findAll().isEmpty()) {
           logger.info("catégorie déjà présentes, seeding ignoré");
           return;
       }
        // 1. Boissons non alcoolisées (softs)
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
        logger.info("Category seeding process completed.");
    }

    private Categorie createCategory(String name, Categorie parent) {
        String baseCode = generateSlug(name);
        String uniqueCode = generateUniqueCode(baseCode);

        return categorieRepository.findByCode(uniqueCode).orElseGet(() -> {
            Categorie categorie = new Categorie();
            categorie.setNom(name);
            categorie.setCodeCategorie(uniqueCode);
            categorie.setParentCategorie(parent);
            Categorie createdCategory = categorieRepository.save(categorie);
            logger.debug("Seeded category: {}", createdCategory.getNom());
            return createdCategory;
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
