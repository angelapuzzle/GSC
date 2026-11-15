package com.gsc.gsc.service;
import org.springframework.stereotype.Service;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.UsuarioRepository;

import com.gsc.gsc.dto.UserCreateRequestDTO;
import com.gsc.gsc.dto.UserPatchRequestDTO;
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

    public Usuario crearUsuario(UserCreateRequestDTO dto) {

        if (usuarioRepository.existsById(dto.getSerialIdentidad())) {
            throw new ResourceAlreadyExistsException("El usuario con serialIdentidad " + dto.getSerialIdentidad() + " ya existe.");
        }

        Usuario usuario = new Usuario();

        usuario.setSerialIdentidad(dto.getSerialIdentidad());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setActivo(dto.getActivo());
        usuario.setCargo(dto.getCargo());

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(UserPatchRequestDTO dto) {
        String serialIdentidad = dto.getSerialIdentidad();
        Usuario usuarioExistente = usuarioRepository.findById(serialIdentidad)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con serialIdentidad " + serialIdentidad + " no existe."));

        if (dto.getNombre() != null) {
            usuarioExistente.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuarioExistente.setApellido(dto.getApellido());
        }
        if (dto.getActivo() != null) {
            usuarioExistente.setActivo(dto.getActivo());
        }
        if (dto.getCargo() != null) {
            usuarioExistente.setCargo(dto.getCargo());
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
