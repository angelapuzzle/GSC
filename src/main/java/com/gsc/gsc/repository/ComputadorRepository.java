package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.ComputadorId;

public interface ComputadorRepository extends JpaRepository<Computador, ComputadorId> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario

}
