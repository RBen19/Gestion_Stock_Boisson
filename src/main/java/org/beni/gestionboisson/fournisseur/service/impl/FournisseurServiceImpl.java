package org.beni.gestionboisson.fournisseur.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.fournisseur.dao.FournisseurRepositoryImpl;
import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.UpdateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;
import org.beni.gestionboisson.fournisseur.exceptions.FournisseurNotFoundException;
import org.beni.gestionboisson.fournisseur.mappers.FournisseurMapper;
import org.beni.gestionboisson.fournisseur.repository.FournisseurRepository;
import org.beni.gestionboisson.fournisseur.service.FournisseurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    private static final Logger logger = LoggerFactory.getLogger(FournisseurServiceImpl.class);

    @Inject
    private FournisseurRepository fournisseurRepository;

    private final Slugify slg = new Slugify();

    @Override
    public FournisseurDTO createFournisseur(CreateFournisseurDTO dto) {
        logger.info("Attempting to create fournisseur with name: {}", dto.getNom());
        Fournisseur fournisseur = FournisseurMapper.toEntity(dto);
        fournisseur.setStatus(true);
        // Generate codeFournisseur
        long maxId = fournisseurRepository.findAll().stream().mapToLong(Fournisseur::getId).max().orElse(0L);
        String code = slg.slugify(dto.getNom()) + "_" + Year.now().getValue() + "_" + (maxId + 1);
        fournisseur.setCodeFournisseur(code.toUpperCase());

        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        logger.info("Fournisseur created successfully with code: {}", savedFournisseur.getCodeFournisseur());
        return FournisseurMapper.toDTO(savedFournisseur);
    }

    @Override
    public List<FournisseurDTO> getAllFournisseurs() {
        logger.info("Fetching all fournisseurs.");
        return fournisseurRepository.findAll().stream()
                .map(FournisseurMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FournisseurDTO getFournisseurByCode(String code) {
        logger.info("Fetching fournisseur by code: {}", code);
        return fournisseurRepository.findByCode(code)
                .map(FournisseurMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Fournisseur with code {} not found.", code);
                    return new FournisseurNotFoundException("Fournisseur not found with code: " + code);
                });
    }

    @Override
    public FournisseurDTO updateFournisseur(String code, UpdateFournisseurDTO dto) {
        logger.info("Attempting to update fournisseur with code: {}", code);
        Fournisseur fournisseur = fournisseurRepository.findByCode(code)
                .orElseThrow(() -> {
                    logger.warn("Fournisseur with code {} not found for update.", code);
                    return new FournisseurNotFoundException("Fournisseur not found with code: " + code);
                });
        FournisseurMapper.updateEntity(dto, fournisseur);
        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);
        logger.info("Fournisseur with code {} updated successfully.", updatedFournisseur.getCodeFournisseur());
        return FournisseurMapper.toDTO(updatedFournisseur);
    }

    @Override
    public void changeStatusByCode(String code, boolean newStatus) {
        logger.info("Attempting to change status for fournisseur with code {}. New status: {}", code, newStatus);
        Fournisseur fournisseur = fournisseurRepository.findByCode(code)
                .orElseThrow(() -> {
                    logger.warn("Fournisseur with code {} not found for status change.", code);
                    return new FournisseurNotFoundException("Fournisseur not found with code: " + code);
                });
        fournisseur.setStatus(newStatus);
        fournisseurRepository.save(fournisseur);
        logger.info("Status for fournisseur with code {} changed successfully to {}.", code, newStatus);
    }
}
