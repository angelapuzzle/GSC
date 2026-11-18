package com.gsc.gsc.service;

import com.gsc.gsc.dto.MonitorAuthDto;
import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.AppUserRepository;
import com.gsc.gsc.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitorAuthenticatorTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MonitorAuthenticator monitorAuthenticator;

    private AppUser monitorUser;

    @BeforeEach
    void setUp() {
        monitorUser = new AppUser();
        monitorUser.setId(1L);
        monitorUser.setUsername("monitor");
        monitorUser.setPassword("$2a$10$hashedPassword");
        monitorUser.setRole("MONITOR");
    }

    @Test
    void autenticarMonitor_exitoso() {
        MonitorAuthDto dto = new MonitorAuthDto(" monitor ", "monitor123", null);
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("monitor123", "$2a$10$hashedPassword")).thenReturn(true);

        AppUser resultado = monitorAuthenticator.autenticarMonitor(dto);

        assertNotNull(resultado);
        assertEquals("monitor", resultado.getUsername());
        assertEquals("MONITOR", resultado.getRole());
    }

    @Test
    void autenticarMonitor_rolConPrefijoRole_exitoso() {
        monitorUser.setRole("ROLE_MONITOR");
        MonitorAuthDto dto = new MonitorAuthDto("monitor", "monitor123", null);
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("monitor123", "$2a$10$hashedPassword")).thenReturn(true);

        AppUser resultado = monitorAuthenticator.autenticarMonitor(dto);

        assertNotNull(resultado);
        assertEquals("ROLE_MONITOR", resultado.getRole());
    }

    @Test
    void autenticarMonitor_contrasenaIncorrecta_lanzaExcepcion() {
        MonitorAuthDto dto = new MonitorAuthDto("monitor", "claveMala", null);
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("claveMala", "$2a$10$hashedPassword")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> monitorAuthenticator.autenticarMonitor(dto));
        assertEquals("Credenciales inválidas.", ex.getMessage());
    }

    @Test
    void autenticarMonitor_rolNoAutorizado_lanzaExcepcion() {
        monitorUser.setRole("USER");
        MonitorAuthDto dto = new MonitorAuthDto("monitor", "monitor123", null);
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("monitor123", "$2a$10$hashedPassword")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> monitorAuthenticator.autenticarMonitor(dto));
        assertEquals("El usuario no cuenta con permisos de Monitor.", ex.getMessage());
    }

    @Test
    void autenticarMonitorConDocumento_sinDocumento_exitoso() {
        MonitorAuthDto dto = new MonitorAuthDto("monitor", "monitor123", "");
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("monitor123", "$2a$10$hashedPassword")).thenReturn(true);

        AppUser resultado = monitorAuthenticator.autenticarMonitorConDocumento(dto);

        assertNotNull(resultado);
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void autenticarMonitorConDocumento_usuarioInactivo_lanzaExcepcion() {
        MonitorAuthDto dto = new MonitorAuthDto("monitor", "monitor123", "123456");
        when(appUserRepository.findByUsername("monitor")).thenReturn(Optional.of(monitorUser));
        when(passwordEncoder.matches("monitor123", "$2a$10$hashedPassword")).thenReturn(true);

        Usuario usuarioInactivo = new Usuario();
        usuarioInactivo.setSerialIdentidad("123456");
        usuarioInactivo.setActivo(false);
        when(usuarioRepository.findById("123456")).thenReturn(Optional.of(usuarioInactivo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> monitorAuthenticator.autenticarMonitorConDocumento(dto));
        assertEquals("El usuario institucional se encuentra inactivo.", ex.getMessage());
    }
}
