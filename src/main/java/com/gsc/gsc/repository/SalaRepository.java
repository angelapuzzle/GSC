package com.gsc.gsc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.model.Sala;


public interface SalaRepository extends JpaRepository<Sala, String> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario
    Optional<Sala> findByResponsable(AppUser responsible);
}
