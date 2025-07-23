package org.beni.gestionboisson.type_mouvement.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.shared.custom.DuplicateEntityException;
import org.beni.gestionboisson.shared.custom.EntityNotFoundException;
import org.beni.gestionboisson.type_mouvement.dao.TypeMouvementRepositoryImpl;
import org.beni.gestionboisson.type_mouvement.dto.TypeMouvementDTO;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;
import org.beni.gestionboisson.type_mouvement.mappers.TypeMouvementMapper;
import org.beni.gestionboisson.type_mouvement.repository.TypeMouvementRepository;
import org.beni.gestionboisson.type_mouvement.service.TypeMouvementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeMouvementServiceImpl implements TypeMouvementService {

    private static final Logger logger = LoggerFactory.getLogger(TypeMouvementServiceImpl.class);

    @Inject
    private TypeMouvementRepository typeMouvementRepository;

    private final Slugify slugify = new Slugify();

    @Override
    public List<TypeMouvementDTO> getAllTypeMouvements() {
        logger.info("Fetching all type mouvements");
        return typeMouvementRepository.findAll().stream()
                .map(TypeMouvementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TypeMouvementDTO getTypeMouvementById(Long id) {
        logger.info("Fetching type mouvement with ID: {}", id);
        return typeMouvementRepository.findById(id)
                .map(TypeMouvementMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Type Mouvement with ID {} not found", id);
                    return new EntityNotFoundException("Type Mouvement with ID " + id + " not found");
                });
    }

    @Override
    @Transactional
    public TypeMouvementDTO createTypeMouvement(TypeMouvementDTO typeMouvementDTO) {
        logger.info("Creating new type mouvement with libelle: {}", typeMouvementDTO.getLibelle());

        if (typeMouvementDTO.getLibelle() == null || typeMouvementDTO.getLibelle().trim().isEmpty()) {
            logger.warn("Invalid input: libelle cannot be null or empty");
            throw new IllegalArgumentException("Libelle cannot be null or empty");
        }

        String generatedCode = slugify.slugify(typeMouvementDTO.getLibelle());

        if (typeMouvementRepository.existsByLibelle(typeMouvementDTO.getLibelle())) {
            logger.warn("Duplicate libelle: A type mouvement with libelle '{}' already exists", typeMouvementDTO.getLibelle());
            throw new DuplicateEntityException("Type Mouvement with libelle '" + typeMouvementDTO.getLibelle() + "' already exists");
        }

        if (typeMouvementRepository.existsByCode(generatedCode)) {
            logger.warn("Duplicate code: A type mouvement with code '{}' already exists", generatedCode);
            throw new DuplicateEntityException("Type Mouvement with code '" + generatedCode + "' already exists");
        }

        TypeMouvement typeMouvement = TypeMouvementMapper.toEntity(typeMouvementDTO);
        typeMouvement.setCode(generatedCode);
        typeMouvement = typeMouvementRepository.save(typeMouvement);
        logger.info("Successfully created type mouvement with ID: {}", typeMouvement.getId());
        return TypeMouvementMapper.toDTO(typeMouvement);
    }

    @Override
    @Transactional
    public TypeMouvementDTO updateTypeMouvement(Long id, TypeMouvementDTO typeMouvementDTO) {
        logger.info("Updating type mouvement with ID: {}", id);

        if (typeMouvementDTO.getLibelle() == null || typeMouvementDTO.getLibelle().trim().isEmpty()) {
            logger.warn("Invalid input: libelle cannot be null or empty for update");
            throw new IllegalArgumentException("Libelle cannot be null or empty");
        }

        TypeMouvement existingTypeMouvement = typeMouvementRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Type Mouvement with ID {} not found for update", id);
                    return new EntityNotFoundException("Type Mouvement with ID " + id + " not found");
                });

        String newLibelle = typeMouvementDTO.getLibelle();
        String newCode = slugify.slugify(newLibelle);

        if (!existingTypeMouvement.getLibelle().equalsIgnoreCase(newLibelle) && typeMouvementRepository.existsByLibelle(newLibelle)) {
            logger.warn("Duplicate libelle: Another type mouvement with libelle '{}' already exists", newLibelle);
            throw new DuplicateEntityException("Type Mouvement with libelle '" + newLibelle + "' already exists");
        }

        if (!existingTypeMouvement.getCode().equalsIgnoreCase(newCode) && typeMouvementRepository.existsByCode(newCode)) {
            logger.warn("Duplicate code: Another type mouvement with code '{}' already exists", newCode);
            throw new DuplicateEntityException("Type Mouvement with code '" + newCode + "' already exists");
        }

        existingTypeMouvement.setLibelle(newLibelle);
        existingTypeMouvement.setCode(newCode);
        existingTypeMouvement = typeMouvementRepository.save(existingTypeMouvement);
        logger.info("Successfully updated type mouvement with ID: {}", existingTypeMouvement.getId());
        return TypeMouvementMapper.toDTO(existingTypeMouvement);
    }

    @Override
    @Transactional
    public void deleteTypeMouvement(Long id) {
        logger.info("Deleting type mouvement with ID: {}", id);
        TypeMouvement typeMouvement = typeMouvementRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Type Mouvement with ID {} not found for deletion", id);
                    return new EntityNotFoundException("Type Mouvement with ID " + id + " not found");
                });
        typeMouvementRepository.delete(typeMouvement);
        logger.info("Successfully deleted type mouvement with ID: {}", id);
    }

    @Override
    @Transactional
    public void seedTypeMouvements() {
        logger.info("Seeding default type mouvements");
        String[][] defaultTypes = {
                {"Réception", "reception"},
                {"Vente", "vente"},
                {"Transfert", "transfert"}
        };

        for (String[] type : defaultTypes) {
            String libelle = type[0];
            String code = slugify.slugify(libelle);

            if (!typeMouvementRepository.existsByLibelle(libelle) && !typeMouvementRepository.existsByCode(code)) {
                TypeMouvement typeMouvement = TypeMouvement.builder()
                        .libelle(libelle)
                        .code(code)
                        .build();
                typeMouvementRepository.save(typeMouvement);
                logger.info("Seeded type mouvement: {} ({})", libelle, code);
            } else {
                logger.warn("Type Mouvement already exists, skipping seeding: {} ({})", libelle, code);
            }
        }
        logger.info("Finished seeding default type mouvements");
    }
}
