package com.gsc.gsc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    @Order(1) // Evalúa esta cadena primero
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            // Aplica ESTA cadena SOLO a las URLs que empiezan con /api/
            .securityMatcher("/api/**") 
            
            // 1. Deshabilitar CSRF (es seguro aquí porque no usamos cookies de sesión)
            .csrf(csrf -> csrf.disable())
            
            // 2. CORS (solo necesario para la API si la consumen desde otros dominios)
            .cors(Customizer.withDefaults())
            
            // 3. Gestión de sesión STATELESS (no creará cookies JSESSIONID para peticiones /api)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 4. Reglas de autorización específicas para la API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .anyRequest().authenticated()
            )
            
            // 5. Inyectar filtro JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/error", "/login", "/logout", "/").permitAll()
                
                .requestMatchers("/sala/registro", "/sala/registrar").hasAnyRole("USER", "ADMIN")
                
                .requestMatchers("/sala/seleccionar", "/sala/monitoreo").hasAnyRole("MONITOR", "ADMIN")

                .requestMatchers("/historial/**").hasAnyRole("MONITOR", "ADMIN")

                .requestMatchers("/template_test").hasAnyRole("USER", "ADMIN")
                
                .anyRequest().hasRole("ADMIN")
            )
            .formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/");
                })
                .permitAll()
            )
            .logout(Customizer.withDefaults());
        return http.build();
    }
}