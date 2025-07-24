package org.beni.gestionboisson.mouvement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.type_mouvement.entities.TypeMouvement;

import java.time.Instant;

@Entity
@Table(name = "mouvements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mouvement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_mouvement_id")
    private TypeMouvement typeMouvement;

    private String notes;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }


}
