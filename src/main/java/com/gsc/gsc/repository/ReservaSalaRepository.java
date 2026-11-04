package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.ReservaSala;

public interface ReservaSalaRepository extends JpaRepository<ReservaSala, Long> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario

}
