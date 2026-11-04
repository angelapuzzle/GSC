package com.gsc.gsc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsc.gsc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Aquí se pueden definir métodos de consulta personalizados si es necesario

}
