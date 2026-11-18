package com.gsc.gsc.config;

import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.model.enums.CargoUsuario;
import com.gsc.gsc.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("La base de datos ya contiene usuarios. Omitiendo inicialización.");
            return;
        }

        log.info("Inicializando datos semilla de usuarios...");

        crearUsuario("1001", "Juan", "Pérez", true, CargoUsuario.ESTUDIANTE);
        crearUsuario("1002", "María", "García", true, CargoUsuario.ESTUDIANTE);
        crearUsuario("1003", "Carlos", "Rodríguez", false, CargoUsuario.ESTUDIANTE);
        crearUsuario("2001", "Ana", "Martínez", true, CargoUsuario.DOCENTE);
        crearUsuario("2002", "Luis", "Hernández", true, CargoUsuario.DOCENTE);
        crearUsuario("3001", "Sofía", "López", true, CargoUsuario.ADMINISTRATIVO);

        log.info("Datos semilla de usuarios inicializados correctamente.");
    }

    private void crearUsuario(String serialIdentidad, String nombre, String apellido, Boolean activo, CargoUsuario cargo) {
        if (usuarioRepository.existsById(serialIdentidad)) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setSerialIdentidad(serialIdentidad);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setActivo(activo);
        usuario.setCargo(cargo);

        usuarioRepository.save(usuario);
        log.info("Usuario '{}' ( {}) creado correctamente.", usuario.getNombreCompleto(), serialIdentidad);
    }
}
