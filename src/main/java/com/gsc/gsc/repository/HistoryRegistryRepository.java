package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.HistoryRegistry;
import java.util.List;

public interface HistoryRegistryRepository extends JpaRepository<HistoryRegistry, Long> {
    // Método personalizado para consultar historial por usuario
    List<HistoryRegistry> findByUsuarioId(String usuarioId);
}