package org.beni.gestionboisson.auth.database.seeders;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AuthSeeder {

    private static final Logger logger = LoggerFactory.getLogger(AuthSeeder.class);

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UtilisateurService utilisateurService;

    //@PostConstruct
    public void init() {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        if (roleRepository.findByCode("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setCode("ADMIN");
            adminRole.setLibelle("Administrateur");
            adminRole.setStatus(true);
            roleRepository.save(adminRole);
            logger.info("Seeded ADMIN role.");
        }
        
        if (roleRepository.findByCode("PERSONNEL").isEmpty()) {
            Role personnelRole = new Role();
            personnelRole.setCode("PERSONNEL");
            personnelRole.setLibelle("Personnel");
            personnelRole.setStatus(true);
            roleRepository.save(personnelRole);
            logger.info("Seeded PERSONNEL role.");
        }
    }

    private void seedUsers() {
        if (!utilisateurService.checkNomUtilisateurExists("rben19")) {
            UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setNomUtilisateur("rben19");
            utilisateurDTO.setEmail("rben19@example.com");
            utilisateurDTO.setPassword("password123");
            utilisateurService.createUtilisateur(utilisateurDTO, "ADMIN");
            logger.info("Seeded user rben19.");
        }
        
        seedPersonnelUsers();
    }
    
    private void seedPersonnelUsers() {
        for (int i = 1; i <= 20; i++) {
            String username = "personnel" + String.format("%02d", i);
            String email = "personnel" + String.format("%02d", i) + "@company.com";
            
            if (!utilisateurService.checkNomUtilisateurExists(username) && 
                !utilisateurService.checkEmailExists(email)) {
                
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
                utilisateurDTO.setNomUtilisateur(username);
                utilisateurDTO.setEmail(email);
                utilisateurDTO.setPassword("personnel123");
                utilisateurService.createUtilisateur(utilisateurDTO, "PERSONNEL");
                logger.info("Seeded user {}.", username);
            }
        }
        logger.info("Finished seeding personnel users.");
    }
}
