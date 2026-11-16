package com.gsc.gsc.config;

import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.service.AppUserAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserAuthService appUserAuthService;

    public DataInitializer(AppUserAuthService appUserAuthService) {
        this.appUserAuthService = appUserAuthService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (appUserAuthService.existsUsers()) {
            log.info("La base de datos ya contiene usuarios. Omitiendo inicialización.");
            return;
        }

        AppUser createdUser;

        createdUser = appUserAuthService.registrarUsuario("admin", "admin123", "ADMIN");
        log.info("Usuario administrador inicial '{}' creado correctamente.", createdUser.getUsername());
        createdUser = appUserAuthService.registrarUsuario("user", "user123", "USER");
        log.info("Usuario administrador inicial '{}' creado correctamente.", createdUser.getUsername());
        createdUser = appUserAuthService.registrarUsuario("monitor", "monitor123", "MONITOR");
        log.info("Usuario administrador inicial '{}' creado correctamente.", createdUser.getUsername());
    }
}