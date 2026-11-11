package com.gsc.gsc.repository;

import com.gsc.gsc.model.ReservaSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<ReservaSala, Long> {

    // Cambió a String (idSala) para que coincida con la entidad Sala
    List<ReservaSala> findBySala_IdSalaAndFecha(String idSala, LocalDate fecha);
}