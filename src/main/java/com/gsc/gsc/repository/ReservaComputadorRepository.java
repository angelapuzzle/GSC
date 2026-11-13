package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.Usuario;
import java.util.Optional;

public interface ReservaComputadorRepository extends JpaRepository<ReservaComputador, Long> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario
    Optional<ReservaComputador> findByUsuarioAndComputadorAndHoraFinIsNull(Usuario usuario, Computador computador);

    boolean existsByUsuarioAndHoraFinIsNull(Usuario usuario);
}
