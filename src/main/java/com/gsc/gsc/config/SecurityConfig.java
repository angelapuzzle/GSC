package com.gsc.gsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        // BCrypt genera hashes seguros con un "cost factor" por defecto (fuerza de trabajo de 10)
        return new BCryptPasswordEncoder();
    }
}
