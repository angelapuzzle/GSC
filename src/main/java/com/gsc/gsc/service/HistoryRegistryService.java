package com.gsc.gsc.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.repository.ReservaComputadorRepository;

@Service
public class HistoryRegistryService {

    private final ReservaComputadorRepository reservaComputadorRepository;

    HistoryRegistryService(ReservaComputadorRepository reservaComputadorRepository) {
        this.reservaComputadorRepository = reservaComputadorRepository;
    }

    public List<ReservaComputador> obtenerHistorial(LocalDate fecha) {
        List<ReservaComputador> reservas = reservaComputadorRepository.findAll();
        if (fecha != null) {
            return reservas.stream()
                    .filter(r -> fecha.equals(r.getFecha()))
                    .collect(Collectors.toList());
        }
        return reservas;
    }

    public Optional<ReservaComputador> obtenerPorId(Long idReserva) {
        return reservaComputadorRepository.findById(idReserva);
    }

    public Optional<ReservaComputador> agregarComentario(Long idReserva, String comentario) {
        return reservaComputadorRepository.findById(idReserva).map(reserva -> {
            reserva.setComentarios(comentario);
            return reservaComputadorRepository.save(reserva);
        });
    }
}