package org.beni.gestionboisson.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.inject.cdi.se.CdiSeInjectionManagerFactory;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        //  Active CDI dans Jersey
        property("jersey.config.inject.InjectManagerFactory", CdiSeInjectionManagerFactory.class.getName());

        //  Scanne toutes tes classes REST, filtres, services
        packages("org.beni.gestionboisson");
    }
}
