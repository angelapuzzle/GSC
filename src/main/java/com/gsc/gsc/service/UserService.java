package com.gsc.gsc.service;
import org.springframework.stereotype.Service;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.UsuarioRepository;

import com.gsc.gsc.dto.UserCreateRequestDTO;
import com.gsc.gsc.dto.UserPatchRequestDTO;
import com.gsc.gsc.exception.ResourceNotFoundException;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;

    UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(UserCreateRequestDTO dto) {
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
}
