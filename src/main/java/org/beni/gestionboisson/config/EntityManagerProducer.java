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
            logger.info("🔧 Configuration de la base de données via DATABASE_URL pour Render");
            
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
                
                logger.info("✅ Base de données Render configurée: " + host + ":" + port + "/" + database);
                
            } catch (Exception e) {
                logger.severe("❌ Erreur lors de la configuration Render: " + e.getMessage());
                throw new RuntimeException("Impossible de configurer la base de données Render", e);
            }
        } else {
            logger.info("🏠 Utilisation de la configuration locale de développement");
            properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/gestion_boisson");
            properties.put("javax.persistence.jdbc.user", "postgres");
            properties.put("javax.persistence.jdbc.password", "Passer");
        }
        
        // Ajouter les propriétés Hibernate
        properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");
        
        // Configuration optimisée pour la production
        properties.put("hibernate.connection.pool_size", "10");
        properties.put("hibernate.c3p0.min_size", "5");
        properties.put("hibernate.c3p0.max_size", "20");
        properties.put("hibernate.c3p0.timeout", "300");
        properties.put("hibernate.c3p0.max_statements", "50");
        properties.put("hibernate.c3p0.idle_test_period", "3000");
        
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