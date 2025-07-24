package org.beni.gestionboisson.uniteDeMesure.service.impl;

import com.github.slugify.Slugify;
//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.uniteDeMesure.dao.UniteDeMesureRepositoryImpl;
import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.exceptions.DuplicateUniteDeMesureException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.InvalidUniteDeMesureRequestException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.UniteDeMesureNotFoundException;
import org.beni.gestionboisson.uniteDeMesure.mappers.UniteDeMesureMapper;
import org.beni.gestionboisson.uniteDeMesure.repository.UniteDeMesureRepository;
import org.beni.gestionboisson.uniteDeMesure.service.UniteDeMesureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UniteDeMesureServiceImpl implements UniteDeMesureService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UniteDeMesureServiceImpl.class);

    private final Slugify slg = new Slugify()
            .withCustomReplacement(" ","-")
            ;

    @Inject
    private UniteDeMesureRepository uniteDeMesureRepository;

    

    @Override
    public List<UniteDeMesureDTO> getAllUnitesDeMesure() {
        logger.info("Fetching all units of measure.");
        List<UniteDeMesure> unites = uniteDeMesureRepository.findAll();
        return unites.stream()
                .map(UniteDeMesureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UniteDeMesureDTO getUniteDeMesureById(Long id) {
        logger.info("Fetching unit of measure by ID: {}", id);
        return uniteDeMesureRepository.findById(id)
                .map(UniteDeMesureMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("Unit of measure with ID {} not found.", id);
                    return new UniteDeMesureNotFoundException("Unit of measure with ID " + id + " not found");
                });
    }

    @Override
    @Transactional
    public UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Attempting to create new unit of measure with libelle: {}", uniteDeMesureDTO.getLibelle());
        if (uniteDeMesureDTO.getLibelle() == null || uniteDeMesureDTO.getLibelle().trim().isEmpty()) {
            logger.error("Invalid input: Libelle cannot be null or empty.");
            throw new InvalidUniteDeMesureRequestException("Libelle cannot be null or empty");
        }

        String generatedCode = slg.slugify(uniteDeMesureDTO.getLibelle()).toUpperCase();
        uniteDeMesureDTO.setCode(generatedCode); // Set the generated code to the DTO for consistency

        Optional<UniteDeMesure> existingUnite = uniteDeMesureRepository.findByCode(generatedCode);
        if (existingUnite.isPresent()) {
            logger.warn("Duplicate entry: Unit of measure with code {} already exists.", generatedCode);
            throw new DuplicateUniteDeMesureException("Unit of measure with code " + generatedCode + " already exists");
        }
        Optional<UniteDeMesure> existingByLibelle = uniteDeMesureRepository.findByLibelle(uniteDeMesureDTO.getLibelle());
        if (existingByLibelle.isPresent()) {
            logger.warn("Duplicate entry: Unit of measure with libelle {} already exists.", uniteDeMesureDTO.getLibelle());
            throw new DuplicateUniteDeMesureException("Unit of measure with libelle " + uniteDeMesureDTO.getLibelle() + " already exists");
        }

        UniteDeMesure uniteDeMesure = UniteDeMesureMapper.toEntity(uniteDeMesureDTO);
        uniteDeMesure = uniteDeMesureRepository.save(uniteDeMesure);
        logger.info("Successfully created unit of measure with ID: {}", uniteDeMesure.getId());
        return UniteDeMesureMapper.toDto(uniteDeMesure);
    }

    @Override
    @Transactional
    public UniteDeMesureDTO updateUniteDeMesure(Long id, UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Attempting to update unit of measure with ID: {}", id);
        if (uniteDeMesureDTO.getLibelle() == null || uniteDeMesureDTO.getLibelle().trim().isEmpty()) {
            logger.error("Invalid input: Libelle cannot be null or empty for update.");
            throw new InvalidUniteDeMesureRequestException("Libelle cannot be null or empty");
        }

        UniteDeMesure existingUnite = uniteDeMesureRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unit of measure with ID {} not found for update.", id);
                    return new UniteDeMesureNotFoundException("Unit of measure with ID " + id + " not found");
                });

        String newCode = slg.slugify(uniteDeMesureDTO.getLibelle());

        // Check for duplicate code if the libelle has changed
        if (!newCode.equals(existingUnite.getCode())) {
            Optional<UniteDeMesure> uniteWithSameCode = uniteDeMesureRepository.findByCode(newCode);
            if (uniteWithSameCode.isPresent() && !uniteWithSameCode.get().getId().equals(id)) {
                logger.warn("Duplicate entry: Another unit of measure with code {} already exists.", newCode);
                throw new DuplicateUniteDeMesureException("Another unit of measure with code " + newCode + " already exists");
            }
        }

        uniteDeMesureDTO.setCode(newCode); // Set the generated code to the DTO for consistency
        UniteDeMesureMapper.updateEntityFromDto(uniteDeMesureDTO, existingUnite);
        existingUnite = uniteDeMesureRepository.save(existingUnite);
        logger.info("Successfully updated unit of measure with ID: {}", existingUnite.getId());
        return UniteDeMesureMapper.toDto(existingUnite);
    }

    @Override
    @Transactional
    public void deleteUniteDeMesure(Long id) {
        logger.info("Attempting to delete unit of measure with ID: {}", id);
        uniteDeMesureRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unit of measure with ID {} not found for deletion.", id);
                    return new UniteDeMesureNotFoundException("Unit of measure with ID " + id + " not found");
                });
        uniteDeMesureRepository.deleteById(id);
        logger.info("Successfully deleted unit of measure with ID: {}", id);
    }

    @Override
    @Transactional
    public void seedUniteDeMesure() {
        if (getAllUnitesDeMesure().isEmpty()) {
            logger.info("Seeding default units of measure.");
            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Litre")
                    .description("Unité principale pour les boissons liquides en grandes quantités (ex: bouteilles de 1L)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Millilitre")
                    .description("Unité utilisée pour les petites contenances (ex: canettes de 330ml, bouteilles de 500ml)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Centilitre")
                    .description("Couramment utilisé pour les spiritueux (ex: 70cl, 75cl)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Bouteille")
                    .description("Unité pour les boissons vendues à l’unité (bouteilles en verre ou plastique)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Canette")
                    .description("Unité spécifique aux boissons conditionnées en canettes")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Fût")
                    .description("Unité utilisée pour les contenants de grande capacité (ex: bière en fût)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Pack")
                    .description("Utilisé pour les packs de boissons (ex: pack de 6 canettes ou bouteilles)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Carton")
                    .description("Conditionnement regroupant plusieurs unités de vente (bouteilles, canettes, packs)")
                    .build());

            createUniteDeMesure(UniteDeMesureDTO.builder()
                    .libelle("Palette")
                    .description("Unité logistique pour les mouvements en gros volumes (stockage ou expédition)")
                    .build());

            logger.info("Default units of measure seeded successfully.");
        } else {
            logger.info("Units of measure already exist, skipping seeding.");
        }
    }
}