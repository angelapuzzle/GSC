package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.ReservaComputador;

public interface ReservaComputadorRepository extends JpaRepository<ReservaComputador, Long> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario

}
