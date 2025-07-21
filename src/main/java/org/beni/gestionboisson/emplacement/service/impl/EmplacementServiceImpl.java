package org.beni.gestionboisson.emplacement.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.emplacement.dto.EmplacementDTO;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.exceptions.DuplicateEmplacementCodeException;
import org.beni.gestionboisson.emplacement.mappers.EmplacementMapper;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.emplacement.service.EmplacementService;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;
import org.beni.gestionboisson.type_emplacement.repository.TypeEmplacementRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmplacementServiceImpl implements EmplacementService {

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
    public EmplacementDTO createEmplacement(EmplacementDTO dto) {
        String generatedCode = generateCodeEmplacement(dto.getNom());
        if (emplacementRepository.findByCodeEmplacement(generatedCode).isPresent()) {
            throw new DuplicateEmplacementCodeException("Emplacement with code " + generatedCode + " already exists.");
        }

        TypeEmplacement typeEmplacement = typeEmplacementRepository.findByCode(dto.getCodeTypeEmplacement())
                .orElseThrow(() -> new RuntimeException("TypeEmplacement with code " + dto.getCodeTypeEmplacement() + " not found."));
        Optional<Emplacement> existingEmplacement = emplacementRepository.findEmplacementByNom(dto.getNom());
        if (existingEmplacement.isPresent()) {
            throw new DuplicateEmplacementCodeException("Emplacement with libelle already exists." + dto.getNom());

        }
        Slugify slugify = new Slugify()
                .withCustomReplacement(" ", "-");



        String slug = slugify.slugify(dto.getNom());
        Emplacement emplacement = EmplacementMapper.toEntity(dto);
        emplacement.setCodeEmplacement(slug);
        emplacement.setType(typeEmplacement);

        return EmplacementMapper.toDTO(emplacementRepository.save(emplacement));
    }

    @Override
    public List<EmplacementDTO> getAllEmplacements() {
        return emplacementRepository.findAll().stream()
                .map(EmplacementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmplacementDTO getEmplacementByCode(String code) {
        return emplacementRepository.findByCodeEmplacement(code)
                .map(EmplacementMapper::toDTO)
                .orElse(null);
    }

    @Override
    public EmplacementDTO updateEmplacement(String code, EmplacementDTO dto) {
        Emplacement existingEmplacement = emplacementRepository.findByCodeEmplacement(code)
                .orElseThrow(() -> new RuntimeException("Emplacement with code " + code + " not found."));

        // If nom is changed, regenerate codeEmplacement and check for duplicates
        if (!existingEmplacement.getNom().equals(dto.getNom())) {
            String newGeneratedCode = generateCodeEmplacement(dto.getNom());
            if (!newGeneratedCode.equals(code) && emplacementRepository.findByCodeEmplacement(newGeneratedCode).isPresent()) {
                throw new DuplicateEmplacementCodeException("Emplacement with code " + newGeneratedCode + " already exists.");
            }
            existingEmplacement.setCodeEmplacement(newGeneratedCode);
        }

        TypeEmplacement typeEmplacement = typeEmplacementRepository.findByCode(dto.getCodeTypeEmplacement())
                .orElseThrow(() -> new RuntimeException("TypeEmplacement with code " + dto.getCodeTypeEmplacement() + " not found."));

        EmplacementMapper.updateEntity(dto, existingEmplacement);
        existingEmplacement.setType(typeEmplacement);

        return EmplacementMapper.toDTO(emplacementRepository.save(existingEmplacement));
    }

    @Override
    public void deleteEmplacement(String code) {
        emplacementRepository.findByCodeEmplacement(code)
                .ifPresent(emplacement -> emplacementRepository.deleteById(emplacement.getId()));
    }

    @Override
    public void seedEmplacements() {
        if (emplacementRepository.findAll().isEmpty()) {
            createEmplacement(EmplacementDTO.builder().nom("Rayon A").codeTypeEmplacement("DISPLAY").build());
            createEmplacement(EmplacementDTO.builder().nom("Frigo 1").codeTypeEmplacement("REFRIGERATOR").build());
            createEmplacement(EmplacementDTO.builder().nom("Zone Réception").codeTypeEmplacement("RECEIVING").build());
            createEmplacement(EmplacementDTO.builder().nom("Stock Principal").codeTypeEmplacement("STOCK").build());
            createEmplacement(EmplacementDTO.builder().nom("Quai Expédition").codeTypeEmplacement("SHIPPING").build());
            System.out.println("Emplacements seeded.");
        }
    }
}