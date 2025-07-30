package org.beni.gestionboisson.mouvement.database.seeders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.emplacement.repository.EmplacementRepository;
import org.beni.gestionboisson.lignemouvement.dto.LigneMouvementCreateDTO;
import org.beni.gestionboisson.lignemouvement.service.LigneMouvementService;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.lot.repository.LotRepository;
import org.beni.gestionboisson.mouvement.dto.MouvementCreateDTO;
import org.beni.gestionboisson.mouvement.dto.MouvementDTO;
import org.beni.gestionboisson.mouvement.service.MouvementService;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;
import org.beni.gestionboisson.type_mouvement.repository.TypeMouvementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class MouvementSeeder {

    private static final Logger logger = LoggerFactory.getLogger(MouvementSeeder.class);

    @Inject
    private MouvementService mouvementService;

    @Inject
    private LigneMouvementService ligneMouvementService;

    @Inject
    private LotRepository lotRepository;

    @Inject
    private TypeMouvementRepository typeMouvementRepository;

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private EmplacementRepository emplacementRepository;

    private final Random random = new Random();

    public void seedMouvements() {
        logger.info("🔄 Début du seeding des mouvements...");

        try {
            // Vérifier si des mouvements existent déjà
            if (!mouvementService.getAllMouvements().isEmpty()) {
                logger.info("⚠️ Des mouvements existent déjà, seeding ignoré");
                return;
            }

            // Récupérer les données de référence
            List<Lot> lots = lotRepository.findAll();
            List<TypeMouvement> typesMouvement = typeMouvementRepository.findAll();
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            List<Emplacement> emplacements = emplacementRepository.findAll();

            if (lots.isEmpty() || typesMouvement.isEmpty() || utilisateurs.isEmpty() || emplacements.isEmpty()) {
                logger.warn("⚠️ Données de référence manquantes pour créer les mouvements");
                return;
            }

            Utilisateur utilisateurDefaut = utilisateurs.stream()
                    .filter(u -> "rben19@example.com".equals(u.getEmail()))
                    .findFirst()
                    .orElse(utilisateurs.get(0));

            // Créer différents types de mouvements pour les statistiques
            createVariousMovements(lots, typesMouvement, utilisateurDefaut, emplacements);

            logger.info("✅ Seeding des mouvements terminé avec succès");

        } catch (Exception e) {
            logger.error("❌ Erreur lors du seeding des mouvements: {}", e.getMessage(), e);
            throw new RuntimeException("Échec du seeding des mouvements", e);
        }
    }

    private void createVariousMovements(List<Lot> lots, List<TypeMouvement> typesMouvement, 
                                      Utilisateur utilisateur, List<Emplacement> emplacements) {
        
        // Créer 20-30 mouvements variés
        int nombreMouvements = 20 + random.nextInt(10);
        
        List<String> notesVariees = Arrays.asList(
            "Réception automatisée",
            "Transfert pour réapprovisionnement",
            "Sortie pour commande client",
            "Contrôle qualité effectué",
            "Rotation des stocks FIFO",
            "Transfert vers zone de vente",
            "Échantillonnage pour test",
            "Ajustement d'inventaire",
            "Retour défectueux",
            "Mouvement d'urgence"
        );

        for (int i = 0; i < nombreMouvements; i++) {
            try {
                TypeMouvement typeMouvement = typesMouvement.get(random.nextInt(typesMouvement.size()));
                String notes = notesVariees.get(random.nextInt(notesVariees.size()));

                // Créer le mouvement principal
                MouvementCreateDTO mouvementDTO = MouvementCreateDTO.builder()
                        .typeMouvementCode(typeMouvement.getCode())
                        .utilisateurEmail(utilisateur.getEmail())
                        .notes(notes + " - Seeder " + (i + 1))
                        .build();

                MouvementDTO mouvementCree = mouvementService.createMouvement(mouvementDTO);

                // Créer 1-3 lignes de mouvement pour ce mouvement
                int nombreLignes = 1 + random.nextInt(3);
                createLignesMouvementForMouvement(mouvementCree, lots, emplacements, nombreLignes);

                if ((i + 1) % 5 == 0) {
                    logger.info("✓ {} mouvements créés...", i + 1);
                }

            } catch (Exception e) {
                logger.warn("⚠️ Erreur création mouvement {}: {}", i + 1, e.getMessage());
            }
        }
    }

    private void createLignesMouvementForMouvement(MouvementDTO mouvement, List<Lot> lots, 
                                                 List<Emplacement> emplacements, int nombreLignes) {
        
        for (int j = 0; j < nombreLignes; j++) {
            try {
                Lot lot = lots.get(random.nextInt(lots.size()));
                Emplacement emplacementSource = emplacements.get(random.nextInt(emplacements.size()));
                Emplacement emplacementDestination = emplacements.get(random.nextInt(emplacements.size()));

                // Quantité entre 10% et 50% de la quantité actuelle du lot
                double pourcentage = 0.1 + random.nextDouble() * 0.4;
                Double quantite = Math.round(lot.getQuantiteActuelle() * pourcentage * 100.0) / 100.0;
                quantite = Math.max(1.0, quantite); // Minimum 1 unité

                LigneMouvementCreateDTO ligneDTO = LigneMouvementCreateDTO.builder()
                        .mouvementId(mouvement.getId())
                        .lotCode(lot.getNumeroLot())
                        .quantite(quantite)
                        .emplacementSourceCode(emplacementSource.getCodeEmplacement())
                        .emplacementDestinationCode(emplacementDestination.getCodeEmplacement())
                        .build();

                ligneMouvementService.createLigneMouvement(ligneDTO);

            } catch (Exception e) {
                logger.warn("⚠️ Erreur création ligne mouvement: {}", e.getMessage());
            }
        }
    }
}