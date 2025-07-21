package org.beni.gestionboisson.emplacement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beni.gestionboisson.type_emplacement.entities.TypeEmplacement;

import java.time.Instant;

@Entity
@Table(name = "emplacements")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Emplacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String codeEmplacement;

    @ManyToOne
    @JoinColumn(name = "type_emplacement_id", nullable = false)
    private TypeEmplacement type;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
