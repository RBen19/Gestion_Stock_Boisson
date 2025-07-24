package org.beni.gestionboisson.type_lot_status.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.type_lot_status.dao.TypeLotStatusRepositoryImpl;
import org.beni.gestionboisson.type_lot_status.dto.TypeLotStatusDTO;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;
import org.beni.gestionboisson.type_lot_status.exceptions.DuplicateEntityException;
import org.beni.gestionboisson.type_lot_status.exceptions.EntityNotFoundException;
import org.beni.gestionboisson.type_lot_status.mappers.TypeLotStatusMapper;
import org.beni.gestionboisson.type_lot_status.service.TypeLotStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeLotStatusServiceImpl implements TypeLotStatusService {

    private static final Logger logger = LoggerFactory.getLogger(TypeLotStatusServiceImpl.class);

    @Inject
    private TypeLotStatusRepositoryImpl typeLotStatusRepository;

    private final Slugify slugify = new Slugify();

    @Override
    @Transactional
    public TypeLotStatusDTO createTypeLotStatus(TypeLotStatusDTO typeLotStatusDTO) {
        logger.info("Attempting to create new TypeLotStatus with libelle: {}", typeLotStatusDTO.getLibelle());

        if (typeLotStatusRepository.findByLibelle(typeLotStatusDTO.getLibelle()).isPresent()) {
            logger.warn("Creation failed: TypeLotStatus with libelle {} already exists.", typeLotStatusDTO.getLibelle());
            throw new DuplicateEntityException("TypeLotStatus with libelle " + typeLotStatusDTO.getLibelle() + " already exists.");
        }

        String slug = slugify.slugify(typeLotStatusDTO.getLibelle());
        if (typeLotStatusRepository.findBySlug(slug).isPresent()) {
            logger.warn("Creation failed: TypeLotStatus with generated slug {} already exists.", slug);
            throw new DuplicateEntityException("TypeLotStatus with generated slug " + slug + " already exists.");
        }

        TypeLotStatus typeLotStatus = TypeLotStatusMapper.toEntity(typeLotStatusDTO);
        typeLotStatus.setSlug(slug);
        typeLotStatus.setId(null); // Ensure ID is null for new entity

        TypeLotStatus savedTypeLotStatus = typeLotStatusRepository.save(typeLotStatus);
        logger.info("Successfully created TypeLotStatus with ID: {}", savedTypeLotStatus.getId());
        return TypeLotStatusMapper.toDTO(savedTypeLotStatus);
    }

    @Override
    @Transactional
    public TypeLotStatusDTO updateTypeLotStatus(Long id, TypeLotStatusDTO typeLotStatusDTO) {
        logger.info("Attempting to update TypeLotStatus with ID: {}", id);

        TypeLotStatus existingTypeLotStatus = typeLotStatusRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Update failed: TypeLotStatus with ID {} not found.", id);
                    return new EntityNotFoundException("TypeLotStatus with ID " + id + " not found.");
                });

        // Check for duplicate libelle, excluding the current entity
        if (typeLotStatusRepository.findByLibelle(typeLotStatusDTO.getLibelle())
                .filter(tls -> !tls.getId().equals(id))
                .isPresent()) {
            logger.warn("Update failed: TypeLotStatus with libelle {} already exists.", typeLotStatusDTO.getLibelle());
            throw new DuplicateEntityException("TypeLotStatus with libelle " + typeLotStatusDTO.getLibelle() + " already exists.");
        }

        String newSlug = slugify.slugify(typeLotStatusDTO.getLibelle());
        // Check for duplicate slug, excluding the current entity
        if (typeLotStatusRepository.findBySlug(newSlug)
                .filter(tls -> !tls.getId().equals(id))
                .isPresent()) {
            logger.warn("Update failed: TypeLotStatus with generated slug {} already exists.", newSlug);
            throw new DuplicateEntityException("TypeLotStatus with generated slug " + newSlug + " already exists.");
        }

        existingTypeLotStatus.setLibelle(typeLotStatusDTO.getLibelle());
        existingTypeLotStatus.setSlug(newSlug);

        TypeLotStatus updatedTypeLotStatus = typeLotStatusRepository.save(existingTypeLotStatus);
        logger.info("Successfully updated TypeLotStatus with ID: {}", updatedTypeLotStatus.getId());
        return TypeLotStatusMapper.toDTO(updatedTypeLotStatus);
    }

    @Override
    @Transactional
    public void deleteTypeLotStatus(Long id) {
        logger.info("Attempting to delete TypeLotStatus with ID: {}", id);
        TypeLotStatus existingTypeLotStatus = typeLotStatusRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Deletion failed: TypeLotStatus with ID {} not found.", id);
                    return new EntityNotFoundException("TypeLotStatus with ID " + id + " not found.");
                });
        typeLotStatusRepository.delete(existingTypeLotStatus);
        logger.info("Successfully deleted TypeLotStatus with ID: {}", id);
    }

    @Override
    public TypeLotStatusDTO getTypeLotStatusById(Long id) {
        logger.info("Fetching TypeLotStatus by ID: {}", id);
        return typeLotStatusRepository.findById(id)
                .map(TypeLotStatusMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("TypeLotStatus with ID {} not found.", id);
                    return new EntityNotFoundException("TypeLotStatus with ID " + id + " not found.");
                });
    }

    @Override
    public TypeLotStatusDTO getTypeLotStatusBySlug(String slug) {
        logger.info("Fetching TypeLotStatus by slug: {}", slug);
        return typeLotStatusRepository.findBySlug(slug)
                .map(TypeLotStatusMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("TypeLotStatus with slug {} not found.", slug);
                    return new EntityNotFoundException("TypeLotStatus with slug " + slug + " not found.");
                });
    }

    @Override
    public List<TypeLotStatusDTO> getAllTypeLotStatuses() {
        logger.info("Fetching all TypeLotStatuses.");
        return typeLotStatusRepository.findAll().stream()
                .map(TypeLotStatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void seedTypeLotStatuses() {
        logger.info("Seeding default TypeLotStatus values.");
        String[] statuses = {"Actif", "En quarantaine", "Refusé", "Expiré", "Terminé"};
        for (String libelle : statuses) {
            if (typeLotStatusRepository.findByLibelle(libelle).isEmpty()) {
                TypeLotStatus typeLotStatus = new TypeLotStatus();
                typeLotStatus.setLibelle(libelle);
                typeLotStatus.setSlug(slugify.slugify(libelle));
                typeLotStatusRepository.save(typeLotStatus);
                logger.info("Seeded TypeLotStatus: {}", libelle);
            } else {
                logger.info("TypeLotStatus '{}' already exists, skipping seeding.", libelle);
            }
        }
        logger.info("Finished seeding default TypeLotStatus values.");
    }
}
