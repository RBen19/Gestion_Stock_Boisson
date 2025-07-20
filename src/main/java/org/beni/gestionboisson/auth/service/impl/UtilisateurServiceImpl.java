package org.beni.gestionboisson.auth.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.mappers.UtilisateurMapper;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;
import org.beni.gestionboisson.auth.service.UtilisateurService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@ApplicationScoped
public class UtilisateurServiceImpl implements UtilisateurService {

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private RoleRepository roleRepository;

    @Override
    public UtilisateurDTO getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurMapper::toDto)
                .orElse(null);
    }

    @Override
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO, String roleCode) {
        Optional<Role> roleOptional = roleRepository.findByCode(roleCode);
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("Role with code " + roleCode + " not found.");
        }
        Role role = roleOptional.get();

        Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setStatus(true);
        utilisateur.setMotDePasseHache(BCrypt.hashpw(utilisateurDTO.getPassword(), BCrypt.gensalt()));
        utilisateur.setRole(role); // Set the found role
        utilisateur = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(utilisateur);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return utilisateurRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean checkNomUtilisateurExists(String nomUtilisateur) {
        return utilisateurRepository.findByNomUtilisateur(nomUtilisateur).isPresent();
    }
}
