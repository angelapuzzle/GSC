package com.gsc.gsc.service;

import com.gsc.gsc.dto.MonitorAuthDto;
import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.AppUserRepository;
import com.gsc.gsc.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonitorAuthenticator {

    private final AppUserRepository appUserRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public MonitorAuthenticator(AppUserRepository appUserRepository,
                                UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica un monitor mediante su usuario de sistema y contraseña.
     *
     * @param dto DTO con las credenciales del monitor
     * @return El AppUser autenticado si las credenciales y el rol son válidos
     * @throws IllegalArgumentException si los datos son vacíos, credenciales inválidas o sin rol de monitor
     */
    public AppUser autenticarMonitor(MonitorAuthDto dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank() ||
            dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Debe ingresar usuario y contraseña.");
        }

        AppUser appUser = appUserRepository.findByUsername(dto.getUsername().trim())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(dto.getPassword(), appUser.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        if (!esRolAutorizado(appUser.getRole())) {
            throw new IllegalArgumentException("El usuario no cuenta con permisos de Monitor.");
        }

        return appUser;
    }

    /**
     * Autentica al monitor y, si se proporciona documento, verifica que exista y esté activo en la tabla Usuario.
     *
     * @param dto DTO con las credenciales del monitor (incluye documento opcional)
     * @return El AppUser autenticado
     * @throws IllegalArgumentException si las credenciales son inválidas o el documento no es válido
     */
    public AppUser autenticarMonitorConDocumento(MonitorAuthDto dto) {
        AppUser appUser = autenticarMonitor(dto);

        if (dto.getDocumento() != null && !dto.getDocumento().isBlank()) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getDocumento().trim());
            if (usuarioOpt.isEmpty()) {
                throw new IllegalArgumentException("El documento de identidad no se encuentra registrado.");
            }
            if (!Boolean.TRUE.equals(usuarioOpt.get().getActivo())) {
                throw new IllegalArgumentException("El usuario institucional se encuentra inactivo.");
            }
        }

        return appUser;
    }

    private boolean esRolAutorizado(String role) {
        if (role == null) {
            return false;
        }
        String roleNormalizado = role.trim().toUpperCase().replace("ROLE_", "");
        return "MONITOR".equals(roleNormalizado) || "ADMIN".equals(roleNormalizado);
    }
}
