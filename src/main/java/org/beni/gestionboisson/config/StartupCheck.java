package org.beni.gestionboisson.config;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StartupCheck {
    public void init(@Observes AfterDeploymentValidation adv, BeanManager bm) {
        System.out.println("CDI is initialized!");
    }
}
