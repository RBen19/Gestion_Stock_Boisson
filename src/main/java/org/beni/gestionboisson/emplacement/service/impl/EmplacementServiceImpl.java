package org.beni.gestionboisson.emplacement.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.exceptions.DuplicateEmplacementCodeException;
import org.beni.gestionboisson.emplacement.exceptions.EmplacementNotFoundException;
import org.beni.gestionboisson.emplacement.mappers.EmplacementMapper;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.emplacement.service.EmplacementService;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;
import org.beni.gestionboisson.type_emplacement.repository.TypeEmplacementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmplacementServiceImpl implements EmplacementService {

    private static final Logger logger = LoggerFactory.getLogger(EmplacementServiceImpl.class);

    @Inject
    EmplacementRepository emplacementRepository;

    @Inject
    TypeEmplacementRepository typeEmplacementRepository;

    private String generateCodeEmplacement(String nom) {
        String cleanedNom = nom.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        String[] words = cleanedNom.split("\\s+");
        StringBuilder codeBuilder = new StringBuilder();

        if (words.length == 1) {
            codeBuilder.append(words[0].substring(0, Math.min(words[0].length(), 3)).toUpperCase());
        } else {
            for (String word : words) {
                if (!word.isEmpty()) {
                    codeBuilder.append(word.charAt(0));
                }
            }
        }
        return codeBuilder.toString().toUpperCase();
    }

    @Override
    @Transactional
    public EmplacementDTO createEmplacement(EmplacementDTO dto) {
        logger.info("Attempting to create emplacement with name: {}", dto.getNom());
        String generatedCode = generateCodeEmplacement(dto.getNom());
        if (emplacementRepository.findByCodeEmplacement(generatedCode).isPresent()) {
            logger.warn("Emplacement creation failed: Code {} already exists.", generatedCode);
            throw new DuplicateEmplacementCodeException("Emplacement with code " + generatedCode + " already exists.");
        }

        TypeEmplacement typeEmplacement = typeEmplacementRepository.findByCode(dto.getCodeTypeEmplacement())
                .orElseThrow(() -> {
                    logger.error("TypeEmplacement with code {} not found.", dto.getCodeTypeEmplacement());
                    return new RuntimeException("TypeEmplacement with code " + dto.getCodeTypeEmplacement() + " not found.");
                });
        Optional<Emplacement> existingEmplacement = emplacementRepository.findEmplacementByNom(dto.getNom());
        if (existingEmplacement.isPresent()) {
            logger.warn("Emplacement creation failed: Name {} already exists.", dto.getNom());
            throw new DuplicateEmplacementCodeException("Emplacement with libelle already exists." + dto.getNom());

        }
        Slugify slugify = new Slugify()
                .withCustomReplacement(" ", "-");

        String slug = slugify.slugify(dto.getNom());
        Emplacement emplacement = EmplacementMapper.toEntity(dto);
        emplacement.setCodeEmplacement(slug);
        emplacement.setType(typeEmplacement);

        Emplacement createdEmplacement = emplacementRepository.save(emplacement);
        logger.info("Emplacement created successfully with code: {}", createdEmplacement.getCodeEmplacement());
        return EmplacementMapper.toDTO(createdEmplacement);
    }

    @Override
    public List<EmplacementDTO> getAllEmplacements() {
        logger.info("Fetching all emplacements.");
        return emplacementRepository.findAll().stream()
                .map(EmplacementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmplacementDTO getEmplacementByCode(String code) {
        logger.info("Fetching emplacement by code: {}", code);
        return emplacementRepository.findByCodeEmplacement(code)
                .map(EmplacementMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Emplacement with code {} not found.", code);
                    return new EmplacementNotFoundException("Emplacement not found with code: " + code);
                });
    }

    @Override
    @Transactional
    public EmplacementDTO updateEmplacement(String code, EmplacementDTO dto) {
        logger.info("Attempting to update emplacement with code: {}", code);
        Emplacement existingEmplacement = emplacementRepository.findByCodeEmplacement(code)
                .orElseThrow(() -> {
                    logger.error("Emplacement with code {} not found for update.", code);
                    return new EmplacementNotFoundException("Emplacement with code " + code + " not found.");
                });

        // If nom is changed, regenerate codeEmplacement and check for duplicates
        if (!existingEmplacement.getNom().equals(dto.getNom())) {
            String newGeneratedCode = generateCodeEmplacement(dto.getNom());
            if (!newGeneratedCode.equals(code) && emplacementRepository.findByCodeEmplacement(newGeneratedCode).isPresent()) {
                logger.warn("Emplacement update failed: New code {} already exists.", newGeneratedCode);
                throw new DuplicateEmplacementCodeException("Emplacement with code " + newGeneratedCode + " already exists.");
            }
            existingEmplacement.setCodeEmplacement(newGeneratedCode);
        }

        TypeEmplacement typeEmplacement = typeEmplacementRepository.findByCode(dto.getCodeTypeEmplacement())
                .orElseThrow(() -> {
                    logger.error("TypeEmplacement with code {} not found for update.", dto.getCodeTypeEmplacement());
                    return new RuntimeException("TypeEmplacement with code " + dto.getCodeTypeEmplacement() + " not found.");
                });

        EmplacementMapper.updateEntity(dto, existingEmplacement);
        existingEmplacement.setType(typeEmplacement);

        Emplacement updatedEmplacement = emplacementRepository.save(existingEmplacement);
        logger.info("Emplacement with code {} updated successfully.", updatedEmplacement.getCodeEmplacement());
        return EmplacementMapper.toDTO(updatedEmplacement);
    }

    @Override
    @Transactional
    public void deleteEmplacement(String code) {
        logger.info("Attempting to delete emplacement with code: {}", code);
        Emplacement emplacementToDelete = emplacementRepository.findByCodeEmplacement(code)
                .orElseThrow(() -> {
                    logger.error("Emplacement with code {} not found for deletion.", code);
                    return new EmplacementNotFoundException("Emplacement not found with code: " + code);
                });
        emplacementRepository.deleteById(emplacementToDelete.getId());
        logger.info("Emplacement with code {} deleted successfully.", code);
    }

    @Override
    @Transactional
    public void seedEmplacements() {
        logger.info("Starting emplacement seeding process.");
        if (emplacementRepository.findAll().isEmpty()) {
            createEmplacement(EmplacementDTO.builder().nom("Rayon A").codeTypeEmplacement("DISPLAY").build());
            createEmplacement(EmplacementDTO.builder().nom("Frigo 1").codeTypeEmplacement("REFRIGERATOR").build());
            createEmplacement(EmplacementDTO.builder().nom("Zone Réception").codeTypeEmplacement("RECEIVING").build());
            createEmplacement(EmplacementDTO.builder().nom("Stock Principal").codeTypeEmplacement("STOCK").build());
            createEmplacement(EmplacementDTO.builder().nom("Quai Expédition").codeTypeEmplacement("SHIPPING").build());
            logger.info("Emplacements seeded successfully.");
        } else {
            logger.info("Emplacements already exist, skipping seeding.");
        }
    }
}