package com.gsc.gsc.service;

import com.gsc.gsc.dto.AsignacionPcDTO;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.ComputadorId;
import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.model.enums.DisponibilidadComputador;
import com.gsc.gsc.repository.ComputadorRepository;
import com.gsc.gsc.repository.ReservaComputadorRepository;
import com.gsc.gsc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomStatusManager {

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaComputadorRepository reservaComputadorRepository;

    public List<Computador> obtenerTodosLosEquipos() {
        return computadorRepository.findAll();
    }

    public List<Computador> obtenerEquiposSinMantenimiento() {
        return computadorRepository.findByDisponibilidadNotOrderBySalaIdSalaAscNumComputadorAsc(
                DisponibilidadComputador.MANTENIMIENTO
        );
    }

    private String extraerIdSala(String username) {
        if (username.startsWith("sala") && username.length() > 4) {
            return username.substring(4); // Ej: "sala1" -> "1"
        }
        if (username.startsWith("monitor") && username.length() > 7) {
            return username.substring(7); // Ej: "monitor1" -> "1"
        }
        return "";
    }

    public List<Computador> obtenerEquiposDeSalaActual(String username) {
        String idSala = extraerIdSala(username);

        if (idSala.isEmpty()) {
            return obtenerEquiposSinMantenimiento();
        }

        return computadorRepository.findBySalaIdSalaAndDisponibilidadNotOrderByNumComputadorAsc(
                idSala,
                DisponibilidadComputador.MANTENIMIENTO
        );
    }

    public List<Computador> obtenerEquiposParaMonitoreo(String username) {
        String idSala = extraerIdSala(username);

        if (idSala.isEmpty()) {
            return obtenerTodosLosEquipos();
        }

        return computadorRepository.findBySalaIdSalaOrderByNumComputadorAsc(idSala);
    }

    public long contarPorEstado(List<Computador> lista, DisponibilidadComputador estado) {
        return lista.stream()
                .filter(pc -> pc.getDisponibilidad() == estado)
                .count();
    }

    @Transactional
    public String procesarIngresoOSalida(AsignacionPcDTO dto) {
        boolean esSalida = "SALIDA".equals(dto.getTipoAccion());

        if (dto.getEquipoSeleccionado() == null || !dto.getEquipoSeleccionado().contains("-")) {
            return esSalida ? "salida_no_registrada" : "no_admitido";
        }

        int guionIndex = dto.getEquipoSeleccionado().indexOf("-");
        String idSala = dto.getEquipoSeleccionado().substring(0, guionIndex);
        Integer numComputador = Integer.parseInt(dto.getEquipoSeleccionado().substring(guionIndex + 1));

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getDocumento());
        Optional<Computador> pcOpt = computadorRepository.findById(new ComputadorId(idSala, numComputador));

        if (usuarioOpt.isEmpty() || pcOpt.isEmpty()) {
            return esSalida ? "salida_no_registrada" : "no_admitido";
        }

        Usuario usuario = usuarioOpt.get();
        Computador pc = pcOpt.get();

        if ("INGRESO".equals(dto.getTipoAccion())) {
            if (pc.getDisponibilidad() != DisponibilidadComputador.DISPONIBLE 
                || reservaComputadorRepository.existsByUsuarioAndHoraFinIsNull(usuario)) {
                return "no_admitido";
            }

            pc.setDisponibilidad(DisponibilidadComputador.OCUPADO);
            computadorRepository.save(pc);

            ReservaComputador reserva = new ReservaComputador();
            reserva.setFecha(LocalDate.now());
            reserva.setHoraInicio(LocalTime.now());
            reserva.setHoraFin(null);
            reserva.setUsuario(usuario);
            reserva.setComputador(pc);
            reservaComputadorRepository.save(reserva);

            return "bienvenido";
        } 
        else if (esSalida) {
            Optional<ReservaComputador> reservaActivaOpt = 
                reservaComputadorRepository.findByUsuarioAndComputadorAndHoraFinIsNull(usuario, pc);

            if (reservaActivaOpt.isEmpty()) {
                return "salida_no_registrada";
            }

            ReservaComputador reserva = reservaActivaOpt.get();
            reserva.setHoraFin(LocalTime.now());
            reservaComputadorRepository.save(reserva);

            pc.setDisponibilidad(DisponibilidadComputador.DISPONIBLE);
            computadorRepository.save(pc);

            return "salida_registrada";
        }

        return "no_admitido";
    }
}
