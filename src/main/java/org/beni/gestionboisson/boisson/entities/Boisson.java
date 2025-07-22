package org.beni.gestionboisson.boisson.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "boissons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Boisson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeBoisson;

    @Column(nullable = false, unique = true)
    private String nom;

   // @Column(nullable = false, unique = true)
  //  private String codeCategorie;

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategorie", referencedColumnName = "idCategorie")
    private Categorie categorie;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
