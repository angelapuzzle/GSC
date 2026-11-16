package com.gsc.gsc.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.service.HistoryRegistryService;
import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/api/historial")
public class HistoryRegistryController {

    @Autowired
    private HistoryRegistryService historyRegistryService;

    @GetMapping
    public List<ReservaComputador> listarHistorial(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return historyRegistryService.obtenerHistorial(fecha);
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ReservaComputador> obtenerDetalleReserva(@PathVariable Long idReserva) {
        return historyRegistryService.obtenerPorId(idReserva)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/historial")
public String verHistorial(
        @RequestParam(required = false) String documento,
        @RequestParam(required = false) String fecha,
        @RequestParam(required = false) String puesto,
        Model model) {
    
    return "views/HistoryViewerGUI";
}

    @PatchMapping("/{idReserva}/comentario")
    public ResponseEntity<ReservaComputador> registrarComentario(
            @PathVariable Long idReserva,
            @RequestBody Map<String, String> payload) {
        String comentario = payload.get("comentario");
        return historyRegistryService.agregarComentario(idReserva, comentario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}