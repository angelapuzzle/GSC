package com.gsc.gsc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public String checkHealth() {
        return "Aplicación Spring Boot activa en Tomcat y conectada a PostgreSQL";
    }
}