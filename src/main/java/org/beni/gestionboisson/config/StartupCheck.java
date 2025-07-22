package org.beni.gestionboisson.config;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class StartupCheck {

    private static final Logger logger = LoggerFactory.getLogger(StartupCheck.class);

    public void init(@Observes AfterDeploymentValidation adv, BeanManager bm) {
        logger.info("CDI is initialized!");
    }
}
