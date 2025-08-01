package org.beni.gestionboisson.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class EntityManagerProducer {
    
    private static final Logger logger = Logger.getLogger(EntityManagerProducer.class.getName());
    private EntityManagerFactory entityManagerFactory;
    
    @PostConstruct
    public void init() {
        Map<String, String> properties = new HashMap<>();
        
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            logger.info("Configuration de la base de données via DATABASE_URL pour Render");
            
            try {
                // Parse de l'URL PostgreSQL de Render
                URI dbUri = new URI(databaseUrl);
                
                String host = dbUri.getHost();
                int port = dbUri.getPort();
                String database = dbUri.getPath().substring(1);
                String[] userInfo = dbUri.getUserInfo().split(":");
                String username = userInfo[0];
                String password = userInfo[1];
                
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, database);
                
                properties.put("javax.persistence.jdbc.url", jdbcUrl);
                properties.put("javax.persistence.jdbc.user", username);
                properties.put("javax.persistence.jdbc.password", password);
                
                logger.info("Base de données Render configurée: " + host + ":" + port + "/" + database);
                
            } catch (Exception e) {
                logger.severe("Erreur lors de la configuration Render: " + e.getMessage());
                throw new RuntimeException("Impossible de configurer la base de données Render", e);
            }
        } else {
            // Configuration via variables d'environnement individuelles
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            
            if (dbUrl != null && dbUser != null && dbPassword != null) {
                logger.info("Configuration via variables d'environnement individuelles");
                properties.put("javax.persistence.jdbc.url", dbUrl);
                properties.put("javax.persistence.jdbc.user", dbUser);
                properties.put("javax.persistence.jdbc.password", dbPassword);
            } else {
                logger.info("Utilisation de la configuration locale de développement par défaut");
                properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/gestion_boisson");
                properties.put("javax.persistence.jdbc.user", "postgres");
                properties.put("javax.persistence.jdbc.password", "Passer");
            }
        }
        
        // Ajouter les propriétés Hibernate
        properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        
        // Configuration Hibernate via variables d'environnement avec valeurs par défaut
        properties.put("hibernate.hbm2ddl.auto", 
            System.getenv("HIBERNATE_HBM2DDL_AUTO") != null ? System.getenv("HIBERNATE_HBM2DDL_AUTO") : "update");
        properties.put("hibernate.show_sql", 
            System.getenv("HIBERNATE_SHOW_SQL") != null ? System.getenv("HIBERNATE_SHOW_SQL") : "false");
        properties.put("hibernate.format_sql", 
            System.getenv("HIBERNATE_FORMAT_SQL") != null ? System.getenv("HIBERNATE_FORMAT_SQL") : "true");
        
        // Configuration du pool de connexions via variables d'environnement
        properties.put("hibernate.connection.pool_size", 
            System.getenv("HIBERNATE_CONNECTION_POOL_SIZE") != null ? System.getenv("HIBERNATE_CONNECTION_POOL_SIZE") : "10");
        properties.put("hibernate.c3p0.min_size", 
            System.getenv("HIBERNATE_C3P0_MIN_SIZE") != null ? System.getenv("HIBERNATE_C3P0_MIN_SIZE") : "5");
        properties.put("hibernate.c3p0.max_size", 
            System.getenv("HIBERNATE_C3P0_MAX_SIZE") != null ? System.getenv("HIBERNATE_C3P0_MAX_SIZE") : "20");
        properties.put("hibernate.c3p0.timeout", 
            System.getenv("HIBERNATE_C3P0_TIMEOUT") != null ? System.getenv("HIBERNATE_C3P0_TIMEOUT") : "300");
        properties.put("hibernate.c3p0.max_statements", 
            System.getenv("HIBERNATE_C3P0_MAX_STATEMENTS") != null ? System.getenv("HIBERNATE_C3P0_MAX_STATEMENTS") : "50");
        properties.put("hibernate.c3p0.idle_test_period", 
            System.getenv("HIBERNATE_C3P0_IDLE_TEST_PERIOD") != null ? System.getenv("HIBERNATE_C3P0_IDLE_TEST_PERIOD") : "3000");
        
        entityManagerFactory = Persistence.createEntityManagerFactory("gestion_boissonPU", properties);
    }
    
    @Produces
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    @PreDestroy
    public void cleanup() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}