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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.auth.exceptions.RoleNotFoundException;

import java.util.Optional;

@ApplicationScoped
public class UtilisateurServiceImpl implements UtilisateurService {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private RoleRepository roleRepository;

    @Override
    public UtilisateurDTO getUtilisateurById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return utilisateurRepository.findById(id)
                .map(UtilisateurMapper::toDto)
                .orElse(null);
    }

    @Override
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO, String roleCode) {
        logger.info("Creating new user with username: {} and role code: {}", utilisateurDTO.getNomUtilisateur(), roleCode);
        Optional<Role> roleOptional = roleRepository.findByCode(roleCode);
        if (roleOptional.isEmpty()) {
            logger.error("Role with code {} not found.", roleCode);
            throw new RoleNotFoundException("Role with code " + roleCode + " not found.");
        }
        Role role = roleOptional.get();

        Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setStatus(true);
        utilisateur.setMotDePasseHache(BCrypt.hashpw(utilisateurDTO.getPassword(), BCrypt.gensalt()));
        utilisateur.setRole(role); // Set the found role
        utilisateur = utilisateurRepository.save(utilisateur);
        logger.info("User created successfully with ID: {}", utilisateur.getIdUtilisateur());
        return UtilisateurMapper.toDto(utilisateur);
    }

    @Override
    public boolean checkEmailExists(String email) {
        logger.info("Checking if email {} exists.", email);
        return utilisateurRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean checkNomUtilisateurExists(String nomUtilisateur) {
        logger.info("Checking if username {} exists.", nomUtilisateur);
        return utilisateurRepository.findByNomUtilisateur(nomUtilisateur).isPresent();
    }
}
