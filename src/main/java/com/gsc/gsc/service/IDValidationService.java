package com.gsc.gsc.service;

import org.springframework.stereotype.Service;

import com.gsc.gsc.exception.InvalidUserStateException;
import com.gsc.gsc.exception.ResourceNotFoundException;
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
        .orElseThrow(() -> new ResourceNotFoundException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));

        if (!usuario.getActivo()) {
            throw new InvalidUserStateException("El usuario con serialIdentidad " + serialIdentidad + " no está activo.");
        }
        
        return usuario;
    }
}
