package org.beni.gestionboisson.boisson.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.boisson.dto.BoissonDTO;
import org.beni.gestionboisson.boisson.entities.Boisson;
import org.beni.gestionboisson.boisson.entities.Categorie;
import org.beni.gestionboisson.boisson.mappers.BoissonMapper;
import org.beni.gestionboisson.boisson.repository.BoissonRepository;
import org.beni.gestionboisson.boisson.repository.CategorieRepository;
import org.beni.gestionboisson.boisson.service.BoissonService;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BoissonServiceImpl implements BoissonService {

    @Inject
    private BoissonRepository boissonRepository;

    @Inject
    private CategorieRepository categorieRepository;

    @Override
    @Transactional
    public BoissonDTO createBoisson(BoissonDTO dto) {
        Categorie categorie = categorieRepository.findByCode(dto.getCodeCategorie())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Categorie Code"));

        long count = boissonRepository.countByCategoryAndUnit(categorie.getCodeCategorie(), dto.getUniteDeMesure());
        String nom = String.format("%s-%s-%03d", categorie.getCodeCategorie().substring(0, 3), dto.getUniteDeMesure(), count + 1);

        Long maxId = boissonRepository.findMaxId().orElse(0L);
        String codeBoisson = String.format("%s-%d--%s", categorie.getCodeCategorie().substring(0, 3), maxId + 1, dto.getUniteDeMesure());

        Boisson boisson = BoissonMapper.toEntity(dto);
        boisson.setNom(nom);
        boisson.setCodeBoisson(codeBoisson);
        boisson.setCategorie(categorie);

        return BoissonMapper.toDTO(boissonRepository.save(boisson));
    }

    @Override
    public List<BoissonDTO> getAllBoissons() {
        return boissonRepository.findAll().stream()
                .map(BoissonMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BoissonDTO getBoissonById(Long id) {
        return boissonRepository.findById(id)
                .map(BoissonMapper::toDTO)
                .orElse(null);
    }
}
