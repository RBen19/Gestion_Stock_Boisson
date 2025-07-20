package org.beni.gestionboisson.auth.database.seeders;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.UtilisateurDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.service.UtilisateurService;

@ApplicationScoped
public class Seeder {

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
            roleRepository.save(adminRole);
            System.out.println("Seeded ADMIN role.");
        }
    }

    private void seedUsers() {
        if (!utilisateurService.checkNomUtilisateurExists("rben19")) {
            UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setNomUtilisateur("rben19");
            utilisateurDTO.setEmail("rben19@example.com");
            utilisateurDTO.setPassword("password123"); // This will be hashed by the service
            utilisateurService.createUtilisateur(utilisateurDTO, "ADMIN");
            System.out.println("Seeded user rben19.");
        }
    }
}
