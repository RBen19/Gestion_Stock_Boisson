package org.beni.gestionboisson.lignemouvement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.lot.entities.Lot;
import org.beni.gestionboisson.mouvement.entities.Mouvement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "ligne_mouvements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LigneMouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mouvement_id", nullable = false)
    private Mouvement mouvement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    private Double quantite;

    @ManyToOne
    @JoinColumn(name = "emplacement_source_id")
    private Emplacement emplacementSource;

    @ManyToOne
    @JoinColumn(name = "emplacement_destination_id")
    private Emplacement emplacementDestination;

    @Column(unique = true, nullable = false)
    private String code; // auto-generated, simple readable format

    @Column(name = "prix_unitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        if (prixUnitaire != null && quantite != null) {
            montantTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
        updatedAt = Instant.now();
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

}
