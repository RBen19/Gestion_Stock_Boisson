package org.beni.gestionboisson.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.util.logging.Logger;

@ApplicationScoped
public class DatabaseConfig {
    
    private static final Logger logger = Logger.getLogger(DatabaseConfig.class.getName());
    
    @PostConstruct
    public void init() {
        configureDatabaseFromEnvironment();
    }
    
    private void configureDatabaseFromEnvironment() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            logger.info("Configuration de la base de données via DATABASE_URL");
            
            try {
                // Parse de l'URL PostgreSQL de Render
                // Format: postgres://username:password@host:port/database
                URI dbUri = new URI(databaseUrl);
                
                String host = dbUri.getHost();
                int port = dbUri.getPort();
                String database = dbUri.getPath().substring(1); // Enlever le '/' initial
                String[] userInfo = dbUri.getUserInfo().split(":");
                String username = userInfo[0];
                String password = userInfo[1];
                
                // Construire l'URL JDBC
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
                
                // Définir les propriétés système pour JPA
                System.setProperty("javax.persistence.jdbc.url", jdbcUrl);
                System.setProperty("javax.persistence.jdbc.user", username);
                System.setProperty("javax.persistence.jdbc.password", password);
                
                logger.info("Base de données configurée: " + jdbcUrl);
                
            } catch (Exception e) {
                logger.severe("Erreur lors de la configuration de la base de données: " + e.getMessage());
                throw new RuntimeException("Impossible de configurer la base de données", e);
            }
        } else {
            logger.info("Utilisation de la configuration locale de développement");
        }
    }
}