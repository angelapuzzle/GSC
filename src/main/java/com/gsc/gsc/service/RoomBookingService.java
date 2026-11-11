package com.gsc.gsc.service;

import com.gsc.gsc.model.ReservaSala;
import com.gsc.gsc.repository.RoomBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class RoomBookingService {

    @Autowired
    private RoomBookingRepository roomBookingRepository;

    private static final LocalTime HORA_APERTURA = LocalTime.of(7, 0);
    private static final LocalTime HORA_CIERRE = LocalTime.of(21, 0);

    /**
     * Crea una nueva reserva validando el horario operativo (07:00 - 21:00) 
     * y evitando superposiciones de horarios en la misma sala y fecha.
     */
    public ReservaSala crearReserva(ReservaSala nuevaReserva) {
        // 1. Validar límites de horario de la sala
        if (nuevaReserva.getHoraInicio().isBefore(HORA_APERTURA) || 
            nuevaReserva.getHoraFin().isAfter(HORA_CIERRE)) {
            throw new IllegalArgumentException("La reserva debe estar estrictamente dentro del horario operativo de 07:00 a 21:00.");
        }

        // 2. Validar que la hora de inicio sea anterior a la de fin
        if (!nuevaReserva.getHoraInicio().isBefore(nuevaReserva.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de finalización.");
        }

        // 3. Consultar reservas existentes usando el idSala como String
        String idSalaStr = nuevaReserva.getSala().getIdSala();
        List<ReservaSala> reservasExistentes = roomBookingRepository.findBySala_IdSalaAndFecha(
            idSalaStr, 
            nuevaReserva.getFecha()
        );

        // 4. Validar superposición de horarios
        boolean hayCruce = reservasExistentes.stream().anyMatch(r -> 
            nuevaReserva.getHoraInicio().isBefore(r.getHoraFin()) && 
            r.getHoraInicio().isBefore(nuevaReserva.getHoraFin())
        );

        if (hayCruce) {
            throw new IllegalStateException("La sala ya cuenta con una reserva activa en ese intervalo de tiempo.");
        }

        // Guardar en la base de datos PostgreSQL
        return roomBookingRepository.save(nuevaReserva);
    }

    /**
     * Obtiene todas las reservas registradas.
     */
    public List<ReservaSala> obtenerTodasLasReservas() {
        return roomBookingRepository.findAll();
    }

    /**
     * Elimina una reserva por su ID.
     */
    public void eliminarReserva(Long idReserva) {
        if (!roomBookingRepository.existsById(idReserva)) {
            throw new IllegalArgumentException("La reserva que intenta eliminar no existe.");
        }
        roomBookingRepository.deleteById(idReserva);
    }
}