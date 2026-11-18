package com.gsc.gsc.repository;

import com.gsc.gsc.model.ReservaSala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ReservaSalaRepository extends JpaRepository<ReservaSala, Long> {

    List<ReservaSala> findBySala_IdSalaAndFecha(String idSala, LocalDate fecha);

}