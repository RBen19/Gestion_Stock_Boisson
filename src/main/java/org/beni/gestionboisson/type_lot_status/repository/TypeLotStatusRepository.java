package org.beni.gestionboisson.type_lot_status.repository;

import org.beni.gestionboisson.type_lot_status.entities.TypeLotStatus;

import java.util.List;
import java.util.Optional;

public interface TypeLotStatusRepository {
    Optional<TypeLotStatus> findById(Long id);
    Optional<TypeLotStatus> findByLibelle(String libelle);
    Optional<TypeLotStatus> findBySlug(String slug);
    List<TypeLotStatus> findAll();
    TypeLotStatus save(TypeLotStatus typeLotStatus);
    void delete(TypeLotStatus typeLotStatus);
}
