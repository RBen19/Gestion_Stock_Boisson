package org.beni.gestionboisson.lot.repository;

import org.beni.gestionboisson.lot.entities.Lot;

import java.util.List;
import java.util.Optional;

public interface LotRepository {
    Optional<Lot> findById(Long id);
    Optional<Lot> findByNumeroLot(String numeroLot);
    List<Lot> findAll();
    Lot save(Lot lot);
    void delete(Long id);
    List<Lot> findAvailableLotsByBoissonCode(String boissonCode);
}
