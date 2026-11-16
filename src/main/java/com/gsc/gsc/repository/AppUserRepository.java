package com.gsc.gsc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsc.gsc.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
