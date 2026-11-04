package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, String> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario

}
