package org.beni.gestionboisson.uniteDeMesure.service.impl;

import com.github.slugify.Slugify;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.uniteDeMesure.dto.UniteDeMesureDTO;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.mappers.UniteDeMesureMapper;
import org.beni.gestionboisson.uniteDeMesure.repository.UniteDeMesureRepository;
import org.beni.gestionboisson.uniteDeMesure.service.UniteDeMesureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UniteDeMesureServiceImpl implements UniteDeMesureService {

    private static final Logger logger = LoggerFactory.getLogger(UniteDeMesureServiceImpl.class);
    private final Slugify slg = new Slugify();

    @Inject
    private UniteDeMesureRepository uniteDeMesureRepository;

    @Override
    @Transactional
    public UniteDeMesureDTO createUniteDeMesure(UniteDeMesureDTO uniteDeMesureDTO) {
        logger.info("Creating new Unite de Mesure with libelle: {}", uniteDeMesureDTO.getLibelle());
        UniteDeMesure uniteDeMesure = UniteDeMesureMapper.toEntity(uniteDeMesureDTO);
        uniteDeMesure.setCode(slg.slugify(uniteDeMesureDTO.getLibelle()));
        UniteDeMesure savedUniteDeMesure = uniteDeMesureRepository.save(uniteDeMesure);
        logger.info("Unite de Mesure created successfully with code: {}", savedUniteDeMesure.getCode());
        return UniteDeMesureMapper.toDTO(savedUniteDeMesure);
    }

    @Override
    public List<UniteDeMesureDTO> getAllUniteDeMesures() {
        logger.info("Fetching all Unite de Mesures");
        return uniteDeMesureRepository.findAll().stream()
                .map(UniteDeMesureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UniteDeMesureDTO getUniteDeMesureById(Long id) {
        logger.info("Fetching Unite de Mesure by ID: {}", id);
        return uniteDeMesureRepository.findById(id)
                .map(UniteDeMesureMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Unite de Mesure not found with ID: " + id));
    }

    @Override
    public UniteDeMesureDTO getUniteDeMesureByCode(String code) {
        logger.info("Fetching Unite de Mesure by code: {}", code);
        return uniteDeMesureRepository.findByCode(code)
                .map(UniteDeMesureMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Unite de Mesure not found with code: " + code));
    }

    @Override
    @Transactional
    public void deleteUniteDeMesure(Long id) {
        logger.info("Deleting Unite de Mesure with ID: {}", id);
        uniteDeMesureRepository.deleteById(id);
        logger.info("Unite de Mesure with ID: {} deleted successfully", id);
    }

    @Override
    @Transactional
    public void seedUniteDeMesure() {
        logger.info("Seeding Unite de Mesure");
        List<UniteDeMesureDTO> uniteDeMesureDTOS = List.of(
                UniteDeMesureDTO.builder().libelle("Canette 33cl").description("Canette en aluminium de 33 centilitres").build(),
                UniteDeMesureDTO.builder().libelle("Bouteille 1L").description("Bouteille en verre ou plastique de 1 litre").build(),
                UniteDeMesureDTO.builder().libelle("Pack de 6").description("Pack de 6 bouteilles ou canettes").build(),
                UniteDeMesureDTO.builder().libelle("Carton de 24").description("Carton de 24 bouteilles ou canettes").build(),
                UniteDeMesureDTO.builder().libelle("Palette").description("Palette de plusieurs cartons").build(),
                UniteDeMesureDTO.builder().libelle("Fontaine 10L").description("Fontaine à boisson de 10 litres").build(),
                UniteDeMesureDTO.builder().libelle("Bag-in-Box").description("Carton avec poche sous vide").build(),
                UniteDeMesureDTO.builder().libelle("Dosette").description("Dosette individuelle pour machine à café ou autre").build(),
                UniteDeMesureDTO.builder().libelle("Poudre 1kg").description("Sachet ou boîte de poudre de 1 kilogramme").build()
        );
        uniteDeMesureDTOS.forEach(this::createUniteDeMesure);
        logger.info("Unite de Mesure seeding completed");
    }
}
