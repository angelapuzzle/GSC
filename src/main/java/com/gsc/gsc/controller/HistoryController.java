package com.gsc.gsc.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.service.HistoryRegistryService;

@Controller
@RequestMapping("/historial")
public class HistoryController {

    private final HistoryRegistryService historyRegistryService;

    HistoryController(HistoryRegistryService historyRegistryService) {
        this.historyRegistryService = historyRegistryService;
    }

    @PreAuthorize ("hasRole('ADMIN') or hasRole('MONITOR')")
    @GetMapping
    public String verHistorial(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Integer puesto,
            Model model) {

        List<ReservaComputador> historial = historyRegistryService.obtenerHistorial(fecha);

        if (historial != null) {
            if (documento != null && !documento.isBlank()) {
                String busqueda = documento.trim().toLowerCase();
                historial = historial.stream()
                        .filter(reg -> reg.getUsuario() != null && (
                                (reg.getUsuario().getSerialIdentidad() != null &&
                                 reg.getUsuario().getSerialIdentidad().toLowerCase().contains(busqueda)) ||
                                (reg.getUsuario().getNombreCompleto() != null &&
                                 reg.getUsuario().getNombreCompleto().toLowerCase().contains(busqueda))
                        ))
                        .collect(Collectors.toList());
            }

            if (fecha != null) {
                historial = historial.stream()
                        .filter(reg -> fecha.equals(reg.getFecha()))
                        .collect(Collectors.toList());
            }

            if (puesto != null) {
                historial = historial.stream()
                        .filter(reg -> reg.getComputador() != null &&
                                puesto.equals(reg.getComputador().getNumComputador()))
                        .collect(Collectors.toList());
            }
        }

        model.addAttribute("historial", historial);
        return "views/HistoryViewerGUI";
    }
}