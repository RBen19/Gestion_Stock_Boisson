package org.beni.gestionboisson.lot.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.repository.BoissonRepository;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;
import org.beni.gestionboisson.fournisseur.repository.FournisseurRepository;
import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.dto.LotStatusUpdateDTO;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.exceptions.DuplicateLotNumeroException;
import org.beni.gestionboisson.lot.exceptions.InvalidLotRequestException;
import org.beni.gestionboisson.lot.exceptions.LotNotFoundException;
import org.beni.gestionboisson.lot.mappers.LotMapper;
import org.beni.gestionboisson.lot.repository.LotRepository;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;
import org.beni.gestionboisson.type_lot_status.repository.TypeLotStatusRepository;
import org.beni.gestionboisson.uniteDeMesure.entities.UniteDeMesure;
import org.beni.gestionboisson.uniteDeMesure.repository.UniteDeMesureRepository;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@ApplicationScoped
public class LotServiceImpl implements org.beni.gestionboisson.lot.service.LotService {

    private static final Logger logger = Logger.getLogger(LotServiceImpl.class.getName());

    @Inject
    private LotRepository lotRepository;

    @Inject
    private BoissonRepository boissonRepository;

    @Inject
    private FournisseurRepository fournisseurRepository;

    

    @Inject
    private TypeLotStatusRepository typeLotStatusRepository;

    @Inject
    private UniteDeMesureRepository uniteDeMesureRepository;

    @Override
    @Transactional
    public LotResponseDTO createLot(LotDTO lotDTO) {
        logger.info("Attempting to create new lot with Boisson Code: " + lotDTO.getBoissonCode());

        // Validate Boisson
        Boisson boisson = boissonRepository.getBoissonByCode(lotDTO.getBoissonCode())
                .orElseThrow(() -> new InvalidLotRequestException("Boisson with code " + lotDTO.getBoissonCode() + " not found."));

        // Validate Fournisseur
        Fournisseur fournisseur = fournisseurRepository.findByCode(lotDTO.getFournisseurCode())
                .orElseThrow(() -> new InvalidLotRequestException("Fournisseur with code " + lotDTO.getFournisseurCode() + " not found."));

        

        // Validate TypeLotStatus
        TypeLotStatus typeLotStatus = typeLotStatusRepository.findByLibelle(lotDTO.getTypeLotStatusCode())
                .orElseThrow(() -> new InvalidLotRequestException("TypeLotStatus with libelle " + lotDTO.getTypeLotStatusCode() + " not found."));

        // Generate unique numeroLot
        String numeroLot = generateLotNumero();
        if (lotRepository.findByNumeroLot(numeroLot).isPresent()) {
            throw new DuplicateLotNumeroException("Generated lot number " + numeroLot + " already exists. Please try again.");
        }

        Lot lot = LotMapper.toEntity(lotDTO);
        lot.setNumeroLot(numeroLot);
        lot.setBoisson(boisson);
        lot.setFournisseur(fournisseur);
        
        lot.setTypeLotStatus(typeLotStatus);
        lot.setQuantiteActuelle(lotDTO.getQuantiteInitiale()); // Initial quantity is current quantity

        Lot savedLot = lotRepository.save(lot);
        logger.info("Lot created successfully with numeroLot: " + savedLot.getNumeroLot());
        return LotMapper.toDTO(savedLot);
    }

    @Override
    public LotResponseDTO getLotById(Long id) {
        logger.info("Attempting to retrieve lot with ID: " + id);
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new LotNotFoundException("Lot with ID " + id + " not found."));
        logger.info("Lot retrieved successfully with ID: " + id);
        return LotMapper.toDTO(lot);
    }

    @Override
    public List<LotResponseDTO> getAllLots() {
        logger.info("Attempting to retrieve all lots.");
        List<LotResponseDTO> lots = lotRepository.findAll().stream()
                .map(LotMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Retrieved " + lots.size() + " lots.");
        return lots;
    }

    @Override
    @Transactional
    public LotResponseDTO updateLot(Long id, LotDTO lotDTO) {
        logger.info("Attempting to update lot with ID: " + id);
        Lot existingLot = lotRepository.findById(id)
                .orElseThrow(() -> new LotNotFoundException("Lot with ID " + id + " not found."));

        // Update fields that are allowed to be changed
        existingLot.setDateAcquisition(lotDTO.getDateAcquisition());
        existingLot.setDateLimiteConsommation(lotDTO.getDateLimiteConsommation());
        existingLot.setQuantiteInitiale(lotDTO.getQuantiteInitiale());
        existingLot.setQuantiteActuelle(lotDTO.getQuantiteActuelle());

        // Validate and update foreign key entities if provided
        if (lotDTO.getBoissonCode() != null) {
            Boisson boisson = boissonRepository.getBoissonByCode(lotDTO.getBoissonCode())
                    .orElseThrow(() -> new InvalidLotRequestException("Boisson with code " + lotDTO.getBoissonCode() + " not found."));
            existingLot.setBoisson(boisson);
        }
        if (lotDTO.getFournisseurCode() != null) {
            Fournisseur fournisseur = fournisseurRepository.findByCode(lotDTO.getFournisseurCode())
                    .orElseThrow(() -> new InvalidLotRequestException("Fournisseur with code " + lotDTO.getFournisseurCode() + " not found."));
            existingLot.setFournisseur(fournisseur);
        }
        
        if (lotDTO.getTypeLotStatusCode() != null) {
            TypeLotStatus typeLotStatus = typeLotStatusRepository.findByLibelle(lotDTO.getTypeLotStatusCode())
                    .orElseThrow(() -> new InvalidLotRequestException("TypeLotStatus with libelle " + lotDTO.getTypeLotStatusCode() + " not found."));
            existingLot.setTypeLotStatus(typeLotStatus);
        }

        Lot updatedLot = lotRepository.save(existingLot);
        logger.info("Lot updated successfully with ID: " + updatedLot.getId());
        return LotMapper.toDTO(updatedLot);
    }

    @Override
    @Transactional
    public void deleteLot(Long id) {
        logger.info("Attempting to delete lot with ID: " + id);
        Lot existingLot = lotRepository.findById(id)
                .orElseThrow(() -> new LotNotFoundException("Lot with ID " + id + " not found."));
        lotRepository.delete(id);
        logger.info("Lot deleted successfully with ID: " + id);
    }

    @Override
    public String generateLotNumero() {
        String datePart = Instant.now().atZone(java.time.ZoneId.systemDefault()).format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = lotRepository.findAll().size() + 1; // Simple counter, ideally more robust
        return String.format("LOT-%s-%04d", datePart, count);
    }

    @Override
    public void validateAvailableQuantity(String boissonCode, Double requestedQty) {
        logger.info("Validating available quantity for boisson code: " + boissonCode + " and requested quantity: " + requestedQty);
        Boisson boisson = boissonRepository.getBoissonByCode(boissonCode)
                .orElseThrow(() -> new InvalidLotRequestException("Boisson with code " + boissonCode + " not found."));

        double totalAvailable = lotRepository.findAvailableLotsByBoissonCode(boissonCode).stream()
                .filter(lot -> lot.getTypeLotStatus().getLibelle().equalsIgnoreCase("actif"))
                .mapToDouble(Lot::getQuantiteActuelle)
                .sum();

        if (totalAvailable < requestedQty) {
            logger.warning("Insufficient quantity for boisson code: " + boissonCode + ". Available: " + totalAvailable + ", Requested: " + requestedQty);
            throw new InvalidLotRequestException("Insufficient quantity available for boisson " + boissonCode + ". Available: " + totalAvailable + ", Requested: " + requestedQty);
        }
        logger.info("Quantity validation successful for boisson code: " + boissonCode);
    }

    @Override
    @Transactional
    public LotResponseDTO changeLotStatus(Long lotId, LotStatusUpdateDTO statusUpdateDTO) {
        logger.info("Attempting to change status for lot ID: " + lotId + " to: " + statusUpdateDTO.getNewStatusLibelle());
        Lot existingLot = lotRepository.findById(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot with ID " + lotId + " not found."));

        TypeLotStatus newStatus = typeLotStatusRepository.findByLibelle(statusUpdateDTO.getNewStatusLibelle())
                .orElseThrow(() -> new InvalidLotRequestException("TypeLotStatus with libelle " + statusUpdateDTO.getNewStatusLibelle() + " not found."));

        existingLot.setTypeLotStatus(newStatus);
        Lot updatedLot = lotRepository.save(existingLot);
        logger.info("Lot status changed successfully for ID: " + lotId + " to: " + newStatus.getLibelle());
        return LotMapper.toDTO(updatedLot);
    }

    @Override
    public List<LotResponseDTO> findAvailableLotsFifo(String boissonCode, Optional<String> uniteDeMesureCode) {
        logger.info("Finding available lots (FIFO) for boisson code: " + boissonCode + ", uniteDeMesureCode: " + uniteDeMesureCode.orElse("N/A"));
        return getSortedLots(boissonCode, uniteDeMesureCode, Comparator.comparing(Lot::getDateAcquisition));
    }

    @Override
    public List<LotResponseDTO> findAvailableLotsLifo(String boissonCode, Optional<String> uniteDeMesureCode) {
        logger.info("Finding available lots (LIFO) for boisson code: " + boissonCode + ", uniteDeMesureCode: " + uniteDeMesureCode.orElse("N/A"));
        return getSortedLots(boissonCode, uniteDeMesureCode, Comparator.comparing(Lot::getDateAcquisition).reversed());
    }

    @Override
    public List<LotResponseDTO> findAvailableLotsFefo(String boissonCode, Optional<String> uniteDeMesureCode) {
        logger.info("Finding available lots (FEFO) for boisson code: " + boissonCode + ", uniteDeMesureCode: " + uniteDeMesureCode.orElse("N/A"));
        return getSortedLots(boissonCode, uniteDeMesureCode, Comparator.comparing(Lot::getDateLimiteConsommation));
    }

    private List<LotResponseDTO> getSortedLots(String boissonCode, Optional<String> uniteDeMesureCode, Comparator<Lot> comparator) {
        Boisson boisson = boissonRepository.getBoissonByCode(boissonCode)
                .orElseThrow(() -> new InvalidLotRequestException("Boisson with code " + boissonCode + " not found."));
        UniteDeMesure unitDeMesureOnDataBase =  uniteDeMesureRepository.findByCode(String.valueOf(uniteDeMesureCode))
                .orElseThrow(() -> new InvalidLotRequestException("No available unitDeMesureOnDataBase found for UnitMesure with  code: " + String.valueOf(uniteDeMesureCode)));
                ;


        List<Lot> availableLots = lotRepository.findAvailableLotsByBoissonCode(boissonCode).stream()
                .filter(lot -> lot.getTypeLotStatus().getLibelle().equalsIgnoreCase("actif"))
                .collect(Collectors.toList());

        if (uniteDeMesureCode.isPresent()) {
            String unitCode = uniteDeMesureCode.get();
            // Assuming Boisson entity has a field for uniteDeMesure or similar
            // Need to validate uniteDeMesureCode against UniteDeMesureRepository
            // For now, just filter based on boisson's unit if it exists
            // This part needs actual implementation based on UniteDeMesure entity and repository
            // For now, I'll assume boisson.getUniteDeMesure() returns the string code
       //  Optional<UniteDeMesure>  unitDeMesureOnDataBase =  uniteDeMesureRepository.findByCode(String.valueOf(uniteDeMesureCode));

             availableLots = availableLots.stream()
                     .filter(lot -> unitDeMesureOnDataBase.getCode().equalsIgnoreCase(unitCode))
                     .collect(Collectors.toList());
             if (availableLots.isEmpty()) {
                 logger.warning("No available lots found for boisson code: " + boissonCode + " and unit code: " + unitCode);
             }


        }

        return availableLots.stream()
                .sorted(comparator)
                .map(LotMapper::toDTO)
                .collect(Collectors.toList());
    }
}
