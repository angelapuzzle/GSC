package com.gsc.gsc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsc.gsc.model.HistoryRegistry;
import com.gsc.gsc.repository.HistoryRegistryRepository;

import java.util.List;

@Service
public class HistoryRegistryService {

    @Autowired
    private HistoryRegistryRepository repository;

    public HistoryRegistry registrar(String usuarioId, String accion, String detalle) {
        HistoryRegistry registro = new HistoryRegistry();
        registro.setUsuarioId(usuarioId);
        registro.setAccion(accion);
        registro.setDetalle(detalle);
        return repository.save(registro);
    }

    public List<HistoryRegistry> obtenerTodos() {
        return repository.findAll();
    }

    public List<HistoryRegistry> obtenerPorUsuario(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}