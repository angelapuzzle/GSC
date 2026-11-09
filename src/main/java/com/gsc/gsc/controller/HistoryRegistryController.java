package com.gsc.gsc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gsc.gsc.model.HistoryRegistry;
import com.gsc.gsc.service.HistoryRegistryService;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistoryRegistryController {

    @Autowired
    private HistoryRegistryService service;

    @GetMapping
    public List<HistoryRegistry> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<HistoryRegistry> obtenerPorUsuario(@PathVariable String usuarioId) {
        return service.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    public HistoryRegistry crear(@RequestBody HistoryRegistry registro) {
        return service.registrar(registro.getUsuarioId(), registro.getAccion(), registro.getDetalle());
    }
}
