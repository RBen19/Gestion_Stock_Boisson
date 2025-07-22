package org.beni.gestionboisson.uniteDeMesure.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.uniteDeMesure.dao.UniteDeMesureRepositoryImpl;
import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.exceptions.DuplicateUniteDeMesureException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.InvalidUniteDeMesureRequestException;
import org.beni.gestionboisson.uniteDeMesure.exceptions.UniteDeMesureNotFoundException;
import org.beni.gestionboisson.uniteDeMesure.mappers.UniteDeMesureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UniteDeMesureService {

    private static final Logger logger = LoggerFactory.getLogger(UniteDeMesureService.class);

    @Inject
    private UniteDeMesureRepositoryImpl uniteDeMesureRepository;

    @Inject
    private UniteDeMesureMapper uniteDeMesureMapper;

    public List<UniteDeMesureDTO> getAllUnitesDeMesure() {
        logger.info("Fetching all units of measure.");
        List<UniteDeMesure> unites = uniteDeMesureRepository.findAll();
        return unites.stream()
                .map(uniteDeMesureMapper::toDto)
                .collect(Collectors.toList());
    }

    public UniteDeMesureDTO getUniteDeMesureById(Long id) {
        logger.info("Fetching unit of measure by ID: {}", id);
        return uniteDeMesureRepository.findById(id)
                .map(uniteDeMesureMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("Unit of measure with ID {} not found.", id);
                    return new UniteDeMesureNotFoundException("Unit of measure with ID " + id + " not found");
                });
    }

    public UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Attempting to create new unit of measure with code: {}", uniteDeMesureDTO.getCode());
        if (uniteDeMesureDTO.getCode() == null || uniteDeMesureDTO.getCode().trim().isEmpty()) {
            logger.error("Invalid input: Code cannot be null or empty.");
            throw new InvalidUniteDeMesureRequestException("Code cannot be null or empty");
        }
        if (uniteDeMesureDTO.getLibelle() == null || uniteDeMesureDTO.getLibelle().trim().isEmpty()) {
            logger.error("Invalid input: Libelle cannot be null or empty.");
            throw new InvalidUniteDeMesureRequestException("Libelle cannot be null or empty");
        }

        Optional<UniteDeMesure> existingUnite = uniteDeMesureRepository.findByCode(uniteDeMesureDTO.getCode());
        if (existingUnite.isPresent()) {
            logger.warn("Duplicate entry: Unit of measure with code {} already exists.", uniteDeMesureDTO.getCode());
            throw new DuplicateUniteDeMesureException("Unit of measure with code " + uniteDeMesureDTO.getCode() + " already exists");
        }

        UniteDeMesure uniteDeMesure = uniteDeMesureMapper.toEntity(uniteDeMesureDTO);
        uniteDeMesure = uniteDeMesureRepository.save(uniteDeMesure);
        logger.info("Successfully created unit of measure with ID: {}", uniteDeMesure.getId());
        return uniteDeMesureMapper.toDto(uniteDeMesure);
    }

    public UniteDeMesureDTO updateUniteDeMesure(Long id, UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Attempting to update unit of measure with ID: {}", id);
        if (uniteDeMesureDTO.getCode() == null || uniteDeMesureDTO.getCode().trim().isEmpty()) {
            logger.error("Invalid input: Code cannot be null or empty for update.");
            throw new InvalidUniteDeMesureRequestException("Code cannot be null or empty");
        }
        if (uniteDeMesureDTO.getLibelle() == null || uniteDeMesureDTO.getLibelle().trim().isEmpty()) {
            logger.error("Invalid input: Libelle cannot be null or empty for update.");
            throw new InvalidUniteDeMesureRequestException("Libelle cannot be null or empty");
        }

        UniteDeMesure existingUnite = uniteDeMesureRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Unit of measure with ID {} not found for update.", id);
                    return new UniteDeMesureNotFoundException("Unit of measure with ID " + id + " not found");
                });

        Optional<UniteDeMesure> uniteWithSameCode = uniteDeMesureRepository.findByCode(uniteDeMesureDTO.getCode());
        if (uniteWithSameCode.isPresent() && !uniteWithSameCode.get().getId().equals(id)) {
            logger.warn("Duplicate entry: Another unit of measure with code {} already exists.", uniteDeMesureDTO.getCode());
            throw new DuplicateUniteDeMesureException("Another unit of measure with code " + uniteDeMesureDTO.getCode() + " already exists");
        }

        uniteDeMesureMapper.updateEntityFromDto(uniteDeMesureDTO, existingUnite);
        existingUnite = uniteDeMesureRepository.save(existingUnite);
        logger.info("Successfully updated unit of measure with ID: {}", existingUnite.getId());
        return uniteDeMesureMapper.toDto(existingUnite);
    }

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
}