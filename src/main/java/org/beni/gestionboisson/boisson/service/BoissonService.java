package org.beni.gestionboisson.boisson.service;

import org.beni.gestionboisson.boisson.dto.BoissonDTO;

import java.util.List;

public interface BoissonService {
    BoissonDTO createBoisson(BoissonDTO dto);
    List<BoissonDTO> getAllBoissons();
    BoissonDTO getBoissonById(Long id);
    void seedBoissons();
    BoissonDTO getBoissonByCode(String codeBoisson);
    BoissonDTO updateBoisson(Long id, BoissonDTO dto);
}
