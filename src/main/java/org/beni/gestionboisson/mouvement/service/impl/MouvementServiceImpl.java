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
import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.dto.TransfertMultipleLotDTO;
import org.beni.gestionboisson.lot.dto.TransfertMultipleLotResponseDTO;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.mappers.LotMapper;
import org.beni.gestionboisson.lot.repository.LotRepository;
import org.beni.gestionboisson.lot.service.LotService;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
    @Inject
    private LotService lotService;



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
    /*
    * Transfert de Lot avec creation de nouveau lot si la quantité n'est pas quantite totale du lot
    * */
    @Override
    public  MouvementDTO transfertLot(String numeroLot, Double quantite, String codeEmplamcementSouce,String codeEmplacementDestination, String UtilisateurEmail, String notes) {
        logger.info("Tentative de transfert de lot with numero lot: {}, quantité: {}", numeroLot, quantite);

        TypeMouvement type = typeMouvementRepository.findByCode("TRANSFERT")
                .orElseThrow(() -> new EntityNotFoundException("TypeMouvement TRANSFERT introuvable"));

        Lot lot = lotRepository.findByNumeroLot(numeroLot)
                .orElseThrow(() -> new EntityNotFoundException("Lot introuvable avec le code : " + numeroLot));

        Emplacement destination = emplacementRepository.findByCodeEmplacement(codeEmplacementDestination)
                .orElseThrow(() -> new EntityNotFoundException("Emplacement introuvable avec code: " + codeEmplacementDestination));
        String sourceLot = lot.getCodeEmplacementActuel();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(UtilisateurEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec email: " + UtilisateurEmail));

        Double quantiteActuelle = lot.getQuantiteActuelle();

        if (quantite == null) {
            quantite = quantiteActuelle;
        }

        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à zéro.");
        }

        if (quantite > quantiteActuelle) {
            throw new IllegalArgumentException("Quantité à transférer supérieure à la quantité disponible.");
        }

        LotDTO lotTransfere;
         LotResponseDTO savedTransfereLot = null;


        if (quantite.equals(quantiteActuelle)) {
            logger.info("Transfert total du lot {}, nouvel emplacement: {}", lot.getNumeroLot(), destination.getCodeEmplacement());
            lot.setCodeEmplacementActuel(destination.getCodeEmplacement());
            lotRepository.save(lot); // Save the original lot with updated location
            savedTransfereLot = LotMapper.toDTO(lot); // For full transfer, the original lot is the "transferred" one
        } else {
            // Transfert partiel → création d’un nouveau lot
        // Save the original lot with reduced quantity

            lotTransfere = LotDTO.builder()
                    .quantiteActuelle(quantite)
                    .dateAcquisition(lot.getDateAcquisition())
                    .dateLimiteConsommation(lot.getDateLimiteConsommation())
                    .boissonCode(lot.getBoisson().getCodeBoisson())
                    .typeLotStatusCode(lot.getTypeLotStatus().getSlug())
                    .quantiteInitiale(quantite)
                    .fournisseurCode(lot.getFournisseur().getCodeFournisseur())
                   // .codeEmplacementActuel(destination.getCodeEmplacement())
                    .codeEmplacementActuel(lot.getCodeEmplacementActuel())
                    .codeEmplacementDestination(destination.getCodeEmplacement())
                    .utilisateurEmail(UtilisateurEmail)
                    .build();
            logger.info("Value of destination.getCodeEmplacement() before building lotTransfere: " + destination.getCodeEmplacement());
            lot.setQuantiteActuelle(quantiteActuelle - quantite);
            lotRepository.save(lot);
            //  System.out.println();
           // System.out.println(codeEmplacementDestination);
            logger.error("-------------------------------------------------------------------------");

            logger.error(destination.getCodeEmplacement());
            logger.error(codeEmplacementDestination);
            logger.info("LotTransfere codeEmplacementActuel before createLot: " + lotTransfere.getCodeEmplacementActuel());

            logger.error("-------------------------------------------------------------------------");

            savedTransfereLot =   lotService.createLot(lotTransfere);

            logger.info("Transfert partiel : {} unités transférées dans un nouveau lot {}", quantite, savedTransfereLot.getNumeroLot());
        }
        // 3. Création du Mouvement
        MouvementCreateDTO mouvementDTO = MouvementCreateDTO.builder()
                .typeMouvementCode(type.getCode())
                .utilisateurEmail(utilisateur.getEmail())
             //   .lotCode
                //   .quantite(quantite)
                .notes(notes)
              //  .destinationCode(destination.getCodeEmplacement())
                .build();

        MouvementDTO createdMouvement = this.createMouvement(mouvementDTO);



        LigneMouvementCreateDTO ligne  = LigneMouvementCreateDTO.builder()
                .mouvementId(createdMouvement.getId())
                .lotCode(savedTransfereLot!=null ? savedTransfereLot.getNumeroLot(): lot.getNumeroLot())
                .quantite(savedTransfereLot!=null ? savedTransfereLot.getQuantiteActuelle(): lot.getQuantiteActuelle())
             //   .emplacementSourceCode(sourceLot)
                .emplacementSourceCode(lot.getCodeEmplacementActuel())
                .emplacementDestinationCode(codeEmplacementDestination)
                .build();
        LigneMouvementDTO createdLigneMouvement  = ligneMouvementService.createLigneMouvement(ligne);
        return createdMouvement;

    }

    @Override
    @Transactional
    public TransfertMultipleLotResponseDTO transfertMultipleLots(TransfertMultipleLotDTO transfertDTO) {
        logger.info("Début du transfert multiple de lots pour la boisson: {}, quantité désirée: {}", 
                   transfertDTO.getBoissonCode(), transfertDTO.getQuantiteTotaleDesire());

        if (transfertDTO.getLots() == null || transfertDTO.getLots().isEmpty()) {
            throw new IllegalArgumentException("La liste des lots ne peut pas être vide");
        }

        if (transfertDTO.getQuantiteTotaleDesire() <= 0) {
            throw new IllegalArgumentException("La quantité désirée doit être supérieure à zéro");
        }

        Emplacement destination = emplacementRepository.findByCodeEmplacement(transfertDTO.getCodeEmplacementDestination())
                .orElseThrow(() -> new EntityNotFoundException("Emplacement introuvable avec code: " + transfertDTO.getCodeEmplacementDestination()));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(transfertDTO.getUtilisateurEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec email: " + transfertDTO.getUtilisateurEmail()));

        TypeMouvement typeMouvement = typeMouvementRepository.findByCode("TRANSFERT_MULTIPLE")
                .or(() -> typeMouvementRepository.findByCode("TRANSFERT"))
                .orElseThrow(() -> new EntityNotFoundException("TypeMouvement TRANSFERT introuvable"));

        MouvementCreateDTO mouvementDTO = MouvementCreateDTO.builder()
                .typeMouvementCode(typeMouvement.getCode())
                .utilisateurEmail(utilisateur.getEmail())
                .notes(transfertDTO.getNotes() + " - Transfert multiple de " + transfertDTO.getLots().size() + " lots")
                .build();

        MouvementDTO mouvementPrincipal = this.createMouvement(mouvementDTO);

        List<TransfertMultipleLotResponseDTO.TransfertLotDetail> detailsTransferts = new ArrayList<>();
        double quantiteRestante = transfertDTO.getQuantiteTotaleDesire();
        double quantiteTransfereeTotale = 0.0;

        for (LotResponseDTO lotDTO : transfertDTO.getLots()) {
            if (quantiteRestante <= 0) {
                break;
            }

            Lot lot = lotRepository.findByNumeroLot(lotDTO.getNumeroLot())
                    .orElseThrow(() -> new EntityNotFoundException("Lot introuvable: " + lotDTO.getNumeroLot()));

            double quantiteDisponible = lot.getQuantiteActuelle();
            double quantiteATransferer = Math.min(quantiteRestante, quantiteDisponible);

            if (quantiteATransferer <= 0) {
                continue; // Lot épuisé, passer au suivant
            }

            boolean transfertTotal = quantiteATransferer >= quantiteDisponible;
            String numeroLotTransfere;
            String emplacementSource = lot.getCodeEmplacementActuel();

            if (transfertTotal) {
                lot.setCodeEmplacementActuel(destination.getCodeEmplacement());
                lotRepository.save(lot);
                numeroLotTransfere = lot.getNumeroLot();
                logger.info("Transfert total du lot {}: {} unités", lot.getNumeroLot(), quantiteATransferer);
            } else {
                LotDTO nouveauLotDTO = LotDTO.builder()
                        .quantiteActuelle(quantiteATransferer)
                        .quantiteInitiale(quantiteATransferer)
                        .dateAcquisition(lot.getDateAcquisition())
                        .dateLimiteConsommation(lot.getDateLimiteConsommation())
                        .boissonCode(lot.getBoisson().getCodeBoisson())
                        .typeLotStatusCode(lot.getTypeLotStatus().getSlug())
                        .fournisseurCode(lot.getFournisseur().getCodeFournisseur())
                        .codeEmplacementActuel(lot.getCodeEmplacementActuel())
                        .codeEmplacementDestination(destination.getCodeEmplacement())
                        .utilisateurEmail(transfertDTO.getUtilisateurEmail())
                        .build();

                lot.setQuantiteActuelle(quantiteDisponible - quantiteATransferer);
                lotRepository.save(lot);

                LotResponseDTO nouveauLot = lotService.createLot(nouveauLotDTO);
                numeroLotTransfere = nouveauLot.getNumeroLot();
                logger.info("Transfert partiel du lot {}: {} unités transférées vers le nouveau lot {}", 
                           lot.getNumeroLot(), quantiteATransferer, numeroLotTransfere);
            }

            LigneMouvementCreateDTO ligneDTO = LigneMouvementCreateDTO.builder()
                    .mouvementId(mouvementPrincipal.getId())
                    .lotCode(numeroLotTransfere)
                    .quantite(quantiteATransferer)
                    .emplacementSourceCode(emplacementSource)
                    .emplacementDestinationCode(destination.getCodeEmplacement())
                    .build();

            ligneMouvementService.createLigneMouvement(ligneDTO);

            TransfertMultipleLotResponseDTO.TransfertLotDetail detail =
                TransfertMultipleLotResponseDTO.TransfertLotDetail.builder()
                    .numeroLotOriginal(lot.getNumeroLot())
                    .numeroLotTransfere(numeroLotTransfere)
                    .quantiteTransferee(quantiteATransferer)
                    .transfertTotal(transfertTotal)
                    .build();

            detailsTransferts.add(detail);

            quantiteRestante -= quantiteATransferer;
            quantiteTransfereeTotale += quantiteATransferer;
        }

        String message = quantiteRestante > 0 
            ? String.format("Transfert partiel effectué: %.2f/%.2f unités transférées. Quantité manquante: %.2f", 
                           quantiteTransfereeTotale, transfertDTO.getQuantiteTotaleDesire(), quantiteRestante)
            : String.format("Transfert complet effectué: %.2f unités transférées sur %d lots", 
                           quantiteTransfereeTotale, detailsTransferts.size());

        logger.info("Transfert multiple terminé: {}", message);

        return TransfertMultipleLotResponseDTO.builder()
                .mouvement(mouvementPrincipal)
                .detailsTransferts(detailsTransferts)
                .quantiteTransfereTotale(quantiteTransfereeTotale)
                .message(message)
                .build();
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
