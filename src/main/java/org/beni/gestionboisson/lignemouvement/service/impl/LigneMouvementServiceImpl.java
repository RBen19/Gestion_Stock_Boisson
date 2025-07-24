package org.beni.gestionboisson.lignemouvement.service.impl;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;
import org.beni.gestionboisson.lignemouvement.entities.LigneMouvement;
import org.beni.gestionboisson.lignemouvement.mappers.LigneMouvementMapper;
import org.beni.gestionboisson.lignemouvement.repository.LigneMouvementRepository;
import org.beni.gestionboisson.lignemouvement.service.LigneMouvementService;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.repository.LotRepository;
import org.beni.gestionboisson.mouvement.entities.Mouvement;
import org.beni.gestionboisson.mouvement.repository.MouvementRepository;
import org.beni.gestionboisson.shared.custom.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class LigneMouvementServiceImpl implements LigneMouvementService {

    private static final Logger logger = LoggerFactory.getLogger(LigneMouvementServiceImpl.class);

    @Inject
    LigneMouvementRepository ligneMouvementRepository;

    @Inject
    MouvementRepository mouvementRepository;

    @Inject
    LotRepository lotRepository;

    @Inject
    EmplacementRepository emplacementRepository;

    @Override
    @Transactional
    public LigneMouvementDTO createLigneMouvement(LigneMouvementCreateDTO ligneMouvementCreateDTO) {
        logger.info("Attempting to create new ligneMouvement for mouvement ID: {}", ligneMouvementCreateDTO.getMouvementId());

        Mouvement mouvement = mouvementRepository.findById(ligneMouvementCreateDTO.getMouvementId())
                .orElseThrow(() -> {
                    logger.warn("Mouvement not found for ID: {}", ligneMouvementCreateDTO.getMouvementId());
                    return new EntityNotFoundException("Mouvement not found with ID: " + ligneMouvementCreateDTO.getMouvementId());
                });

        Lot lot = lotRepository.findByNumeroLot(ligneMouvementCreateDTO.getLotCode())
                .orElseThrow(() -> {
                    logger.warn("Lot not found for code: {}", ligneMouvementCreateDTO.getLotCode());
                    return new EntityNotFoundException("Lot not found with code: " + ligneMouvementCreateDTO.getLotCode());
                });

        Emplacement emplacementSource = null;
        if (ligneMouvementCreateDTO.getEmplacementSourceCode() != null && !ligneMouvementCreateDTO.getEmplacementSourceCode().isEmpty()) {
            emplacementSource = emplacementRepository.findByCodeEmplacement(ligneMouvementCreateDTO.getEmplacementSourceCode())
                    .orElseThrow(() -> {
                        logger.warn("Emplacement source not found for code: {}", ligneMouvementCreateDTO.getEmplacementSourceCode());
                        return new EntityNotFoundException("Emplacement source not found with code: " + ligneMouvementCreateDTO.getEmplacementSourceCode());
                    });
        }

        Emplacement emplacementDestination = null;
        if (ligneMouvementCreateDTO.getEmplacementDestinationCode() != null && !ligneMouvementCreateDTO.getEmplacementDestinationCode().isEmpty()) {
            emplacementDestination = emplacementRepository.findByCodeEmplacement(ligneMouvementCreateDTO.getEmplacementDestinationCode())
                    .orElseThrow(() -> {
                        logger.warn("Emplacement destination not found for code: {}", ligneMouvementCreateDTO.getEmplacementDestinationCode());
                        return new EntityNotFoundException("Emplacement destination not found with code: " + ligneMouvementCreateDTO.getEmplacementDestinationCode());
                    });
        }

        LigneMouvement ligneMouvement = LigneMouvementMapper.toEntity(ligneMouvementCreateDTO);
        ligneMouvement.setMouvement(mouvement);
        ligneMouvement.setLot(lot);
        ligneMouvement.setEmplacementSource(emplacementSource);
        ligneMouvement.setEmplacementDestination(emplacementDestination);
        ligneMouvement.setCode(generateLigneMouvementCode());

        LigneMouvement savedLigneMouvement = ligneMouvementRepository.save(ligneMouvement);
        logger.info("LigneMouvement created successfully with code: {}", savedLigneMouvement.getCode());
        return LigneMouvementMapper.toDTO(savedLigneMouvement);
    }

    @Override
    public LigneMouvementDTO getLigneMouvementById(Long id) {
        logger.info("Attempting to retrieve ligneMouvement with ID: {}", id);
        return ligneMouvementRepository.findById(id)
                .map(LigneMouvementMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("LigneMouvement not found for ID: {}", id);
                    return new EntityNotFoundException("LigneMouvement not found with ID: " + id);
                });
    }

    @Override
    public List<LigneMouvementDTO> getAllLigneMouvements() {
        logger.info("Retrieving all ligneMouvements");
        return ligneMouvementRepository.findAll().stream()
                .map(LigneMouvementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneMouvementDTO> getLigneMouvementsByMouvementId(Long mouvementId) {
        logger.info("Retrieving ligneMouvements for mouvement ID: {}", mouvementId);
        return ligneMouvementRepository.findByMouvementId(mouvementId).stream()
                .map(LigneMouvementMapper::toDTO)
                .collect(Collectors.toList());
    }

    private String generateLigneMouvementCode() {
        return "LIGNE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
