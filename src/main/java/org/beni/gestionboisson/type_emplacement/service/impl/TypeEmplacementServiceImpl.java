package org.beni.gestionboisson.type_emplacement.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.type_emplacement.dto.TypeEmplacementDTO;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;
import org.beni.gestionboisson.type_emplacement.mappers.TypeEmplacementMapper;
import org.beni.gestionboisson.type_emplacement.repository.TypeEmplacementRepository;
import org.beni.gestionboisson.type_emplacement.service.TypeEmplacementService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeEmplacementServiceImpl implements TypeEmplacementService {

    @Inject
    TypeEmplacementRepository typeEmplacementRepository;

    @Override
    public TypeEmplacementDTO createTypeEmplacement(TypeEmplacementDTO dto) {
        Optional<TypeEmplacement> existingTypeByLibelle = typeEmplacementRepository.findByLibelle(dto.getLibelle());
        if (existingTypeByLibelle.isPresent()) {
            throw new RuntimeException("TypeEmplacement with Libelle " + dto.getLibelle() + " already exists.");
        }
        Optional<TypeEmplacement> existingType = typeEmplacementRepository.findByCode(dto.getCode());
        if (existingType.isPresent()) {
            throw new RuntimeException("TypeEmplacement with code " + dto.getCode() + " already exists.");
        }
        TypeEmplacement typeEmplacement = TypeEmplacementMapper.toEntity(dto);
        return TypeEmplacementMapper.toDTO(typeEmplacementRepository.save(typeEmplacement));
    }

    @Override
    public List<TypeEmplacementDTO> getAllTypeEmplacements() {
        return typeEmplacementRepository.findAll().stream()
                .map(TypeEmplacementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TypeEmplacementDTO getTypeEmplacementByCode(String code) {
        return typeEmplacementRepository.findByCode(code)
                .map(TypeEmplacementMapper::toDTO)
                .orElse(null);
    }

    @Override
    public TypeEmplacementDTO updateTypeEmplacement(String code, TypeEmplacementDTO dto) {
        TypeEmplacement existingType = typeEmplacementRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("TypeEmplacement with code " + code + " not found."));

        if (!existingType.getCode().equals(dto.getCode())) {
            if (typeEmplacementRepository.findByCode(dto.getCode()).isPresent()) {
                throw new RuntimeException("TypeEmplacement with code " + dto.getCode() + " already exists.");
            }
        }

        TypeEmplacementMapper.updateEntity(dto, existingType);
        return TypeEmplacementMapper.toDTO(typeEmplacementRepository.save(existingType));
    }

    @Override
    public void deleteTypeEmplacement(String code) {
        typeEmplacementRepository.findByCode(code)
                .ifPresent(typeEmplacement -> typeEmplacementRepository.deleteById(typeEmplacement.getId()));
    }

    @Override
    public void seedTypeEmplacements() {
        if (typeEmplacementRepository.findAll().isEmpty()) {
            createTypeEmplacement(TypeEmplacementDTO.builder().code("STOCK").libelle("Stockage").description("Zone de stockage générale").build());
            createTypeEmplacement(TypeEmplacementDTO.builder().code("REFRIGERATOR").libelle("Réfrigérateur").description("Zone de stockage réfrigérée").build());
            createTypeEmplacement(TypeEmplacementDTO.builder().code("DISPLAY").libelle("Présentoir").description("Zone d'exposition des produits").build());
            createTypeEmplacement(TypeEmplacementDTO.builder().code("RECEIVING").libelle("Réception").description("Zone de réception des marchandises").build());
            createTypeEmplacement(TypeEmplacementDTO.builder().code("SHIPPING").libelle("Expédition").description("Zone d'expédition des marchandises").build());
            System.out.println("TypeEmplacements seeded.");
        }
    }
}
