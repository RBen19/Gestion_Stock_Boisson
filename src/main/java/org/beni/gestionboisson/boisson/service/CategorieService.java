package org.beni.gestionboisson.boisson.service;

import org.beni.gestionboisson.boisson.dto.CategorieDTO;

import java.util.List;

public interface CategorieService {
    List<CategorieDTO> getAllCategories();
    CategorieDTO getCategorieById(Long id);
    void seedCategories();
}
