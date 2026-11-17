package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.ComputadorId;
import com.gsc.gsc.model.enums.DisponibilidadComputador;
import java.util.List;

public interface ComputadorRepository extends JpaRepository<Computador, ComputadorId> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario
    List<Computador> findBySalaIdSalaAndDisponibilidadNotOrderByNumComputadorAsc(
        String idSala, 
        DisponibilidadComputador disponibilidad
    );
    List<Computador> findByDisponibilidadNotOrderBySalaIdSalaAscNumComputadorAsc(
        DisponibilidadComputador disponibilidad
    );
    List<Computador> findBySalaIdSalaOrderByNumComputadorAsc(String idSala);
}
