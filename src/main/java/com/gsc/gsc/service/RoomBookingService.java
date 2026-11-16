package com.gsc.gsc.service;

import com.gsc.gsc.dto.RoomBookingDto;
import com.gsc.gsc.model.ReservaSala;
import com.gsc.gsc.model.Sala;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.RoomBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;

    private static final LocalTime HORA_APERTURA = LocalTime.of(7, 0);  // 7:00 AM
    private static final LocalTime HORA_CIERRE = LocalTime.of(20, 0);   // 8:00 PM

    public RoomBookingService(RoomBookingRepository roomBookingRepository) {
        this.roomBookingRepository = roomBookingRepository;
    }

    @Transactional
    public ReservaSala registrarReserva(RoomBookingDto dto) {
        validarDto(dto);
        ReservaSala nuevaReserva = convertirDtoAEntidad(dto);
        return roomBookingRepository.save(nuevaReserva);
    }

    // =========================================================================
    // 1. VALIDACIÓN INTEGRAL DEL DTO
    // =========================================================================

    public void validarDto(RoomBookingDto dto) {
        if (dto.getIdSala() == null || dto.getIdSala().isBlank()) {
            throw new IllegalArgumentException("Debe ingresar el ID de la sala.");
        }
        if (dto.getSerialIdentidad() == null || dto.getSerialIdentidad().isBlank()) {
            throw new IllegalArgumentException("Debe ingresar el serial o ID del usuario.");
        }
        if (dto.getFecha() == null || dto.getHoraInicio() == null || dto.getHoraFin() == null) {
            throw new IllegalArgumentException("La fecha, hora de inicio y hora de fin son obligatorias.");
        }

        if (!existeSala(dto.getIdSala().trim())) {
            throw new IllegalArgumentException("No existe ninguna sala registrada con el ID: " + dto.getIdSala());
        }
        if (!existeUsuario(dto.getSerialIdentidad().trim())) {
            throw new IllegalArgumentException("No existe ningún usuario registrado con el ID: " + dto.getSerialIdentidad());
        }

        if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de finalización.");
        }
        if (!estaEnHorarioHabil(dto.getHoraInicio(), dto.getHoraFin())) {
            throw new IllegalArgumentException("Horario no permitido: las reservas solo pueden realizarse entre las 7:00 AM y las 8:00 PM.");
        }
        if (haySobrelapamiento(dto.getIdSala().trim(), dto.getFecha(), dto.getHoraInicio(), dto.getHoraFin())) {
            throw new IllegalStateException("La sala ya está reservada en ese rango horario.");
        }
    }

    // =========================================================================
    // 2. CONVERSIÓN PURA (DTO -> ENTIDAD)
    // =========================================================================

    public ReservaSala convertirDtoAEntidad(RoomBookingDto dto) {
        Sala sala = new Sala();
        sala.setIdSala(dto.getIdSala().trim());

        Usuario usuario = new Usuario();
        usuario.setSerialIdentidad(dto.getSerialIdentidad().trim());

        ReservaSala reserva = new ReservaSala();
        reserva.setSala(sala);
        reserva.setUsuario(usuario);
        reserva.setFecha(dto.getFecha());
        reserva.setHoraInicio(dto.getHoraInicio());
        reserva.setHoraFin(dto.getHoraFin());
        reserva.setComentarios(dto.getComentarios());

        if (dto.getSerialesInvitados() != null && !dto.getSerialesInvitados().isBlank()) {
            Set<String> invitados = new HashSet<>();
            for (String serial : dto.getSerialesInvitados().split(",")) {
                String limpio = serial.trim();
                if (!limpio.isEmpty()) {
                    invitados.add(limpio);
                }
            }
            reserva.setSerialesInvitados(invitados);
        }

        return reserva;
    }

    // =========================================================================
    // FUNCIONES AUXILIARES DE REGLAS Y ESTRUCTURA
    // =========================================================================

    public boolean existeSala(String idSala) {
        // TODO: Conectar con salaRepository.existsById(idSala)
        return true;
    }

    public boolean existeUsuario(String serialIdentidad) {
        // TODO: Conectar con usuarioRepository.existsById(serialIdentidad)
        return true;
    }

    public boolean estaEnHorarioHabil(LocalTime horaInicio, LocalTime horaFin) {
        return !horaInicio.isBefore(HORA_APERTURA) && !horaFin.isAfter(HORA_CIERRE);
    }

    public boolean haySobrelapamiento(String idSala, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        List<ReservaSala> reservasExistentes = roomBookingRepository.findBySala_IdSalaAndFecha(idSala, fecha);

        for (ReservaSala existente : reservasExistentes) {
            if (horaInicio.isBefore(existente.getHoraFin()) &&
                existente.getHoraInicio().isBefore(horaFin)) {
                return true;
            }
        }
        return false;
    }
}