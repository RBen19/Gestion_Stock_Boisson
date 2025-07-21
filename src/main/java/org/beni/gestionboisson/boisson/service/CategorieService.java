package org.beni.gestionboisson.boisson.service;

import org.beni.gestionboisson.boisson.dto.CategorieDTO;

import java.util.List;

public interface CategorieService {
    List<CategorieDTO> getAllCategories();
    CategorieDTO getCategorieById(Long id);
    CategorieDTO createCategorie(CategorieDTO categorieDTO);
    CategorieDTO updateCategorie(Long id, CategorieDTO categorieDTO);
    void deleteCategorie(Long id);
    CategorieDTO getCategorieByCode(String codeCategorie);
    void seedCategories();
}
