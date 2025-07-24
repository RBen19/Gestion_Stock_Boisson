package org.beni.gestionboisson.lot.service;

import org.beni.gestionboisson.lot.dto.LotDTO;
import org.beni.gestionboisson.lot.dto.LotResponseDTO;
import org.beni.gestionboisson.lot.dto.LotSearchStrategyDTO;
import org.beni.gestionboisson.lot.dto.LotStatusUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface LotService {
    LotResponseDTO createLot(LotDTO lotDTO);
    LotResponseDTO getLotById(Long id);
    List<LotResponseDTO> getAllLots();
    LotResponseDTO updateLot(Long id, LotDTO lotDTO);
    void deleteLot(Long id);
    String generateLotNumero();
    void validateAvailableQuantity(String boissonCode, Double requestedQty);
    LotResponseDTO changeLotStatus(Long lotId, LotStatusUpdateDTO statusUpdateDTO);
    List<LotResponseDTO> findAvailableLotsFifo(String boissonCode, Optional<String> uniteDeMesureCode);
    List<LotResponseDTO> findAvailableLotsLifo(String boissonCode, Optional<String> uniteDeMesureCode);
    List<LotResponseDTO> findAvailableLotsFefo(String boissonCode, Optional<String> uniteDeMesureCode);
}
