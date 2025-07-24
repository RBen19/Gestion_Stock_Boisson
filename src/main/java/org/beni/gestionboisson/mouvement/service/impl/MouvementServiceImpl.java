package org.beni.gestionboisson.mouvement.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;
import org.beni.gestionboisson.emplacement.dao.EmplacementRepositoryImpl;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.lignemouvement.dao.LigneMouvementRepositoryImpl;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementDTO;
import org.beni.gestionboisson.lignemouvement.repository.LigneMouvementRepository;
import org.beni.gestionboisson.lignemouvement.service.LigneMouvementService;
import org.beni.gestionboisson.lot.dao.LotRepositoryImpl;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.repository.LotRepository;
import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;
import org.beni.gestionboisson.mouvement.entities.Mouvement;
import org.beni.gestionboisson.mouvement.mappers.MouvementMapper;
import org.beni.gestionboisson.mouvement.repository.MouvementRepository;
import org.beni.gestionboisson.mouvement.service.MouvementService;
import org.beni.gestionboisson.shared.custom.EntityNotFoundException;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;
import org.beni.gestionboisson.type_mouvement.repository.TypeMouvementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MouvementServiceImpl implements MouvementService {

    private static final Logger logger = LoggerFactory.getLogger(MouvementServiceImpl.class);

    @Inject
    MouvementRepository mouvementRepository;

    @Inject
    TypeMouvementRepository typeMouvementRepository;

    @Inject
    UtilisateurRepository utilisateurRepository;
    @Inject
    private LotRepository lotRepository;
    @Inject
    private EmplacementRepository emplacementRepository;
    @Inject
    private LigneMouvementRepository ligneMouvementRepository;
    @Inject
    private LigneMouvementService ligneMouvementService;
    @Inject
    private MouvementMapper mouvementMapper;


    @Override
    @Transactional
    public MouvementDTO createMouvement(MouvementCreateDTO mouvementCreateDTO) {
        logger.info("Attempting to create new mouvement with typeMouvementCode: {}", mouvementCreateDTO.getTypeMouvementCode());

        TypeMouvement typeMouvement = typeMouvementRepository.findByCode(mouvementCreateDTO.getTypeMouvementCode())
                .orElseThrow(() -> {
                    logger.warn("TypeMouvement not found for code: {}", mouvementCreateDTO.getTypeMouvementCode());
                    return new EntityNotFoundException("TypeMouvement not found with code: " + mouvementCreateDTO.getTypeMouvementCode());
                });

        Utilisateur utilisateur = utilisateurRepository.findByEmail(mouvementCreateDTO.getUtilisateurEmail())
                .orElseThrow(() -> {
                    logger.warn("Utilisateur not found for user email: {}", mouvementCreateDTO.getUtilisateurEmail());
                    return new EntityNotFoundException("Utilisateur not found with user email: " + mouvementCreateDTO.getUtilisateurEmail());
                });

        Mouvement mouvement = MouvementMapper.toEntity(mouvementCreateDTO);
        mouvement.setTypeMouvement(typeMouvement);
        mouvement.setUtilisateur(utilisateur);
        mouvement.setCode(generateMouvementCode(typeMouvement.getCode()));

        Mouvement savedMouvement = mouvementRepository.save(mouvement);
        logger.info("Mouvement created successfully with code: {}", savedMouvement.getCode());
        return MouvementMapper.toDTO(savedMouvement);
    }

    @Override
    public MouvementDTO getMouvementById(Long id) {
        logger.info("Attempting to retrieve mouvement with ID: {}", id);
        return mouvementRepository.findById(id)
                .map(MouvementMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Mouvement not found for ID: {}", id);
                    return new EntityNotFoundException("Mouvement not found with ID: " + id);
                });
    }

    @Override
    public List<MouvementDTO> getAllMouvements() {
        logger.info("Retrieving all mouvements");
        return mouvementRepository.findAll().stream()
                .map(MouvementMapper::toDTO)
                .collect(Collectors.toList());
    }
    // à appeler sur lotService
    @Override
    public MouvementDTO receptionnerLot(Long lotId, Double quantite, String codeEmplacementDestination, String utilisateurEmail, String notes) {
    //    String code = "receptionLot-" + lotId + "-" + quantite + "-" + codeEmplacementDestination + "-" + utilisateurEmail;

        TypeMouvement type = typeMouvementRepository.findByCode("RECEPTION")
                .orElseThrow(() -> new EntityNotFoundException("TypeMouvement RECEPTION introuvable"));

        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new EntityNotFoundException("Lot introuvable avec ID: " + lotId));

        Emplacement destination = emplacementRepository.findByCodeEmplacement(codeEmplacementDestination)
                .orElseThrow(() -> new EntityNotFoundException("Emplacement introuvable avec code: " + codeEmplacementDestination));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(utilisateurEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec email: " + utilisateurEmail));

        MouvementCreateDTO mouvement =  MouvementCreateDTO.builder()
                .notes(notes)
                .typeMouvementCode(type.getCode())
                .utilisateurEmail(utilisateur.getEmail()).build()
                ;
       MouvementDTO createdMouvement =  this.createMouvement(mouvement);


        LigneMouvementCreateDTO ligne =  LigneMouvementCreateDTO.builder()
                .mouvementId(createdMouvement.getId())
                .lotCode(lot.getNumeroLot())
                .quantite(quantite)
                .emplacementDestinationCode(codeEmplacementDestination)
                .build();
       LigneMouvementDTO createdLigneMouvement  = ligneMouvementService.createLigneMouvement(ligne);

      /*
      *   ligne.setMouvement(mouvement);
        ligne.setLot(lot);
        ligne.setQuantite(quantite);
        ligne.setCode(UUID.randomUUID().toString());
        ligne.setEmplacementDestination(destination);
        ligne.setCreatedAt(new Date());
        ligne.setUpdatedAt(Instant.now());
        ligneMouvementRepository.save(ligne);
      * */

     //   lot.setQuantiteActuelle(lot.getQuantiteActuelle() + quantite);
     //   lotRepository.save(lot);

        return createdMouvement;
    }

    @Override
    public Integer getAllMouvementCount() {
        return mouvementRepository.getAllMouvementCount();
    }

    private String generateMouvementCode(String typeMouvementCode) {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = mouvementRepository.getAllMouvementCount();
        return String.format("MOV-%s-00"+count+"-%s", datePart, typeMouvementCode);
    }
}
