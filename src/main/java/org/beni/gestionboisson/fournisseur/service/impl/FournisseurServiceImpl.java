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
import org.beni.gestionboisson.fournisseur.mappers.FournisseurMapper;
import org.beni.gestionboisson.fournisseur.repository.FournisseurRepository;
import org.beni.gestionboisson.fournisseur.service.FournisseurService;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    @Inject
    private FournisseurRepository fournisseurRepository;

    private final Slugify slg = Slugify.builder().build();

    @Override
    public FournisseurDTO createFournisseur(CreateFournisseurDTO dto) {
        Fournisseur fournisseur = FournisseurMapper.toEntity(dto);
        fournisseur.setStatus(true);
        // Generate codeFournisseur
        long maxId = fournisseurRepository.findAll().stream().mapToLong(Fournisseur::getId).max().orElse(0L);
        String code = slg.slugify(dto.getNom()) + "_" + Year.now().getValue() + "_" + (maxId + 1);
        fournisseur.setCodeFournisseur(code.toUpperCase());

        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
        return FournisseurMapper.toDTO(savedFournisseur);
    }

    @Override
    public List<FournisseurDTO> getAllFournisseurs() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FournisseurDTO getFournisseurByCode(String code) {
        return fournisseurRepository.findByCode(code)
                .map(FournisseurMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Fournisseur not found with code: " + code));
    }

    @Override
    public FournisseurDTO updateFournisseur(String code, UpdateFournisseurDTO dto) {
        Fournisseur fournisseur = fournisseurRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Fournisseur not found with code: " + code));
        FournisseurMapper.updateEntity(dto, fournisseur);
        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);
        return FournisseurMapper.toDTO(updatedFournisseur);
    }

    @Override
    public void changeStatusByCode(String code, boolean newStatus) {
        Fournisseur fournisseur = fournisseurRepository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Fournisseur not found with code: " + code));
        fournisseur.setStatus(newStatus);
        fournisseurRepository.save(fournisseur);
    }
}
