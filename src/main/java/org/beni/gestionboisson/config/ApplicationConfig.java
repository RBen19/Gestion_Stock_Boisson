package org.beni.gestionboisson.config;

import jakarta.ws.rs.ApplicationPath;
import org.beni.gestionboisson.HelloResource;
import org.beni.gestionboisson.auth.security.JwtFilter;
import org.beni.gestionboisson.auth.security.JwtUtil;
import org.beni.gestionboisson.auth.service.impl.AuthServiceImpl;
import org.glassfish.jersey.server.ResourceConfig;

/*
* @ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        //  Scanne toutes tes classes REST, filtres, services
        packages("org.beni.gestionboisson");
        packages("org.beni.gestionboisson.auth");
        packages("org.beni.gestionboisson.type_lot_status");
        packages("org.beni.gestionboisson.emplacement");
        packages("org.beni.gestionboisson.boisson");
        register(HelloResource.class);
        register(JwtFilter.class);       // filtre @Provider
        register(JwtUtil.class);         // utilitaire, doit être injectable
        register(AuthServiceImpl.class); // service
        register(JacksonContextResolver.class);
       // register(org.beni.gestionboisson.type_lot_status.controller.TypeLotStatusController.class);
    }
}
* */

