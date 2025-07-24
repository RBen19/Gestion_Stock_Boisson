package org.beni.gestionboisson.lot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.emplacement.entities.Emplacement;
import org.beni.gestionboisson.fournisseur.entities.Fournisseur;
import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lots")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroLot;

    @ManyToOne
    @JoinColumn(name = "boisson_id", nullable = false)
    private Boisson boisson;

    @Column(nullable = false)
    private Instant dateAcquisition;


    @Column(nullable = false)
    private Instant dateLimiteConsommation;

    @Column(nullable = false)
    private Double quantiteInitiale;

    @Column(nullable = false)
    private Double quantiteActuelle;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "type_lot_status_id", nullable = false)
    private TypeLotStatus typeLotStatus;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
