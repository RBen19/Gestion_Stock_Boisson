package org.beni.gestionboisson.auth.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.entities.Utilisateur;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;
import org.beni.gestionboisson.auth.repository.UtilisateurRepository;
import org.beni.gestionboisson.auth.security.JwtUtil;
import org.beni.gestionboisson.auth.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) throws PasswordChangeRequiredException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        logger.info("Hashed password from DB for {}: {}", authRequestDTO.getEmail(), utilisateur.getMotDePasseHache());

        if (!utilisateur.isStatus()) {
            throw new RuntimeException("User is not active");
        }

        if (utilisateur.getCreatedAt().equals(utilisateur.getUpdatedAt())) {
            throw new PasswordChangeRequiredException("Password change required. Please update your password.");
        }

        if (BCrypt.checkpw(authRequestDTO.getPassword(), utilisateur.getMotDePasseHache())) {
            String token = jwtUtil.generateToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(),utilisateur.getEmail());
            return new AuthResponseDTO(token);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public AuthResponseDTO changePassword(ChangePasswordRequestDTO request) {
      //  String plainPassword = "password123";

        // 2. Génération du hash (avec salt intégré automatiquement)
     //   String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // 3. Affiche le hash généré
       // System.out.println("Generated Hash: " + hashedPassword);

        // 4. Vérifie que le mot de passe correspond au hash
      //  boolean isMatch = BCrypt.checkpw(request.getOldPassword(), hashedPassword);
      //  System.out.println("Password Match: " + isMatch);

       // String hashFromDB = "$2a$10$KLi5rqA31lzO661s75VLoui0s.Cf6O52aOk6q41qTEO6i7UYjwe5a";
        //System.out.println("Match test = " + BCrypt.checkpw("password123", hashFromDB));

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        logger.info("Hashed password from DB for {}: {}", request.getEmail(), utilisateur.getMotDePasseHache());
        System.out.println(request.getNewPassword());
        boolean match = BCrypt.checkpw("password123", "$2a$10$a3/DnQqScYzmadUR52f/HOiO50FYfG.blUQ/cvNj07/kjPNsgHYmK");
        System.out.println("Match = " + match);

        if (!BCrypt.checkpw(request.getOldPassword(), utilisateur.getMotDePasseHache())) {
            throw new RuntimeException("Invalid old password.");
        }
        String hashFromDB = "$2a$10$Qe.DZ2QJfgws2rhVei2pMOWltpihM8ao61khKqb3wIZB5zTrcshaS";
        System.out.println("Match test = " + BCrypt.checkpw("newSecurePassword456", hashFromDB));



        utilisateur.setMotDePasseHache(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        utilisateur.setUpdatedAt(Instant.now()); // Update updatedAt to reflect password change
        utilisateurRepository.save(utilisateur);

        String token = jwtUtil.generateToken(utilisateur.getNomUtilisateur(), utilisateur.getRole().getCode(),utilisateur.getEmail());
        return new AuthResponseDTO(token);
    }
}
