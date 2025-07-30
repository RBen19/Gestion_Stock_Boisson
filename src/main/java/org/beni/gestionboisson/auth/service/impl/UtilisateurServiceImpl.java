package org.beni.gestionboisson.auth.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.dto.UtilisateurListDTO;
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
import org.beni.gestionboisson.auth.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        utilisateur.setStatus(false);
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

    @Override
    public UtilisateurDTO toggleUserStatus(String email) {
        logger.info("Toggling status for user with email: {}", email);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);
        
        if (utilisateurOptional.isEmpty()) {
            logger.error("User with email {} not found", email);
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        
        Utilisateur utilisateur = utilisateurOptional.get();
        boolean currentStatus = utilisateur.isStatus();
        utilisateur.setStatus(!currentStatus);
        
        utilisateur = utilisateurRepository.save(utilisateur);
        logger.info("User status toggled successfully for email: {}. New status: {}", email, utilisateur.isStatus());
        
        return UtilisateurMapper.toDto(utilisateur);
    }

    @Override
    public List<UtilisateurListDTO> getAllUtilisateurs() {
        logger.info("Fetching all users with their roles");
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        
        logger.info("Found {} users", utilisateurs.size());
        return utilisateurs.stream()
                .map(UtilisateurMapper::toListDto)
                .collect(Collectors.toList());
    }
}
