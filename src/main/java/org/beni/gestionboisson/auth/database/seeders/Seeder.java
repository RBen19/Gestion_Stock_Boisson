package org.beni.gestionboisson.auth.database.seeders;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Seeder {

    private static final Logger logger = LoggerFactory.getLogger(Seeder.class);

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private UtilisateurService utilisateurService;

    @PostConstruct
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
    }

    private void seedUsers() {
        if (!utilisateurService.checkNomUtilisateurExists("rben19")) {
            UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setNomUtilisateur("rben19");
            utilisateurDTO.setEmail("rben19@example.com");
            utilisateurDTO.setPassword("password123"); // This will be hashed by the service
            utilisateurService.createUtilisateur(utilisateurDTO, "ADMIN");
            logger.info("Seeded user rben19.");
        }
    }
}
