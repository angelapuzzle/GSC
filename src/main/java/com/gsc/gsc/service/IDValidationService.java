package com.gsc.gsc.service;

import org.springframework.stereotype.Service;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.UsuarioRepository;


@Service 
public class IDValidationService {

    private final UsuarioRepository usuarioRepository;

    IDValidationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario validateActiveUserBySerialIdentidad(String serialIdentidad) {
        Usuario usuario = usuarioRepository.findById(serialIdentidad)
        .orElseThrow(() -> new IllegalArgumentException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("El usuario con serialIdentidad " + serialIdentidad + " no está activo.");
        }
        
        return usuario;
    }
}
