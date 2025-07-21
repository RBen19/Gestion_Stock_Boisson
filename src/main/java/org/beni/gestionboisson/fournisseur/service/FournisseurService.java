package org.beni.gestionboisson.fournisseur.service;

import org.beni.gestionboisson.fournisseur.dto.CreateFournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.FournisseurDTO;
import org.beni.gestionboisson.fournisseur.dto.UpdateFournisseurDTO;

import java.util.List;

public interface FournisseurService {
    FournisseurDTO createFournisseur(CreateFournisseurDTO dto);
    List<FournisseurDTO> getAllFournisseurs();
    FournisseurDTO getFournisseurByCode(String code);
    FournisseurDTO updateFournisseur(String code, UpdateFournisseurDTO dto);
    void changeStatusByCode(String code, boolean newStatus);
}
