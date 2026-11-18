package com.gsc.gsc.service;
import org.springframework.stereotype.Service;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.UsuarioRepository;

import com.gsc.gsc.dto.UserDtos.UserCreateRequestDto;
import com.gsc.gsc.dto.UserDtos.UserPatchRequestDto;
import com.gsc.gsc.exception.InvalidUserStateException;
import com.gsc.gsc.exception.ResourceAlreadyExistsException;
import com.gsc.gsc.exception.ResourceNotFoundException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario obtenerUsuario(String serialIdentidad) {
        return usuarioRepository.findById(serialIdentidad)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));
    }

    public void validarUsuarioActivo(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (usuario.getActivo() == null || !usuario.getActivo()) {
            throw new InvalidUserStateException("El usuario con serialIdentidad " + usuario.getSerialIdentidad() + " no está activo.");
        }
    }
    public void validarUsuarioActivo(String serialIdentidad) {
        Usuario usuario = obtenerUsuario(serialIdentidad);
        validarUsuarioActivo(usuario);
    }

    public Usuario crearUsuario(UserCreateRequestDto dto) {

        if (usuarioRepository.existsById(dto.serialIdentidad())) {
            throw new ResourceAlreadyExistsException("El usuario con serialIdentidad " + dto.serialIdentidad() + " ya existe.");
        }

        Usuario usuario = new Usuario();

        usuario.setSerialIdentidad(dto.serialIdentidad());
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setActivo(dto.activo());
        usuario.setCargo(dto.cargo());

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(UserPatchRequestDto dto) {
        String serialIdentidad = dto.serialIdentidad();
        Usuario usuarioExistente = usuarioRepository.findById(serialIdentidad)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));

        if (dto.nombre() != null) {
            usuarioExistente.setNombre(dto.nombre());
        }
        if (dto.apellido() != null) {
            usuarioExistente.setApellido(dto.apellido());
        }
        if (dto.activo() != null) {
            usuarioExistente.setActivo(dto.activo());
        }
        if (dto.cargo() != null) {
            usuarioExistente.setCargo(dto.cargo());
        }

        // 3. Guardamos los cambios
        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(String serialIdentidad) {
        Usuario usuarioExistente = usuarioRepository.findById(serialIdentidad)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));

        usuarioRepository.delete(usuarioExistente);
    }
}
