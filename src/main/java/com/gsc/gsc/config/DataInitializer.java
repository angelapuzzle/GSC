package com.gsc.gsc.config;

import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.ReservaComputador;
import com.gsc.gsc.model.ReservaSala;
import com.gsc.gsc.model.Sala;
import com.gsc.gsc.model.Usuario;
import com.gsc.gsc.repository.ComputadorRepository;
import com.gsc.gsc.repository.ReservaComputadorRepository;
import com.gsc.gsc.repository.ReservaSalaRepository;
import com.gsc.gsc.repository.SalaRepository;
import com.gsc.gsc.repository.UsuarioRepository;
import com.gsc.gsc.service.AppUserAuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserAuthService appUserAuthService;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final ComputadorRepository computadorRepository;
    private final ReservaComputadorRepository reservaComputadorRepository;
    private final ReservaSalaRepository reservaSalaRepository;

    public DataInitializer(AppUserAuthService appUserAuthService,
                           UsuarioRepository usuarioRepository,
                           SalaRepository salaRepository,
                           ComputadorRepository computadorRepository,
                           ReservaComputadorRepository reservaComputadorRepository,
                           ReservaSalaRepository reservaSalaRepository) {
        this.appUserAuthService = appUserAuthService;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.computadorRepository = computadorRepository;
        this.reservaComputadorRepository = reservaComputadorRepository;
        this.reservaSalaRepository = reservaSalaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createAppUsers();
        createSalas();
        createDummyData();
    }

    private void createAppUsers() {
        if (appUserAuthService.existsUsers()) {
            log.info("La base de datos ya contiene usuarios. Omitiendo inicialización.");
            return;
        }

        AppUser createdUser;

        createdUser = appUserAuthService.registrarUsuario("admin", "admin123", "ADMIN");
        log.info("Usuario administrador inicial '{}' creado correctamente.", createdUser.getUsername());

        createdUser = appUserAuthService.registrarUsuario("monitor1", "monitor1123", "MONITOR");
        log.info("Usuario '{}' creado correctamente.", createdUser.getUsername());
        createdUser = appUserAuthService.registrarUsuario("monitor2", "monitor2123", "MONITOR");
        log.info("Usuario '{}' creado correctamente.", createdUser.getUsername());
        createdUser = appUserAuthService.registrarUsuario("monitor3", "monitor3123", "MONITOR");
        log.info("Usuario '{}' creado correctamente.", createdUser.getUsername());
        createdUser = appUserAuthService.registrarUsuario("monitor4", "monitor4123", "MONITOR");
        log.info("Usuario '{}' creado correctamente.", createdUser.getUsername());
    }

    private void createSalas() {
        if (salaRepository.count() > 0) {
            return;
        }

        List<AppUser> responsables = new ArrayList<>();
        responsables.add(appUserAuthService.registrarUsuario("sala1", "sala1123", "USER"));
        responsables.add(appUserAuthService.registrarUsuario("sala2", "sala2123", "USER"));
        responsables.add(appUserAuthService.registrarUsuario("sala3", "sala3123", "USER"));
        responsables.add(appUserAuthService.registrarUsuario("sala4", "sala4123", "USER"));

        List<Sala> salas = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Sala sala = new Sala();
            sala.setIdSala("SALA-" + i);
            sala.setDisponibilidad(com.gsc.gsc.model.enums.DisponibilidadSala.values()[0]);
            sala.setResponsable(responsables.get(i - 1));
            salas.add(sala);
        }
        salaRepository.saveAll(salas);
    }

    private void createDummyData() {
        if (usuarioRepository.count() > 0) {
            return;
        }

        List<Usuario> usuarios = new ArrayList<>();
        String[] nombres = {"Ana", "Luis", "Carlos", "María", "Jorge", "Laura", "Sofía", "Andrés", "Camila", "Daniel"};
        String[] apellidos = {"García", "Rodríguez", "Martínez", "López", "Hernández", "Gómez", "Pérez", "Torres", "Ramírez", "Díaz"};

        for (int i = 0; i < nombres.length; i++) {
            Usuario usuario = new Usuario();
            usuario.setSerialIdentidad(String.valueOf(1000000000L + i));
            usuario.setNombre(nombres[i]);
            usuario.setApellido(apellidos[i]);
            usuario.setActivo(true);
            usuario.setCargo(com.gsc.gsc.model.enums.CargoUsuario.values()[i % com.gsc.gsc.model.enums.CargoUsuario.values().length]);
            usuarios.add(usuario);
        }
        usuarios = usuarioRepository.saveAll(usuarios);

        Sala[] salas = {
            salaRepository.findById("SALA-1").orElseThrow(),
            salaRepository.findById("SALA-2").orElseThrow(),
            salaRepository.findById("SALA-3").orElseThrow(),
            salaRepository.findById("SALA-4").orElseThrow()
        };

        for (int i = 1; i <= 4; i++) {
            Sala sala = salas[i - 1];

            for (int num = 1; num <= 10; num++) {
                Computador computador = new Computador();
                computador.setNumComputador(num);
                computador.setDisponibilidad(com.gsc.gsc.model.enums.DisponibilidadComputador.values()[0]);
                sala.addComputador(computador);
            }
        }

        List<Computador> computadores = computadorRepository.findAll();

        List<ReservaComputador> reservasComputador = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Usuario usuario = usuarios.get(i % usuarios.size());
            Computador computador = computadores.get(i % computadores.size());

            ReservaComputador reserva = new ReservaComputador();
            reserva.setFecha(LocalDate.now().plusDays(i % 14));
            reserva.setHoraInicio(LocalTime.of(8 + (i % 8), 0));
            reserva.setHoraFin(reserva.getHoraInicio().plusHours(1));
            reserva.setComentarios("Reserva de prueba " + (i + 1));
            reserva.setUsuario(usuario);
            reserva.setComputador(computador);
            reservasComputador.add(reserva);
        }
        reservaComputadorRepository.saveAll(reservasComputador);

        List<ReservaSala> reservasSala = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ReservaSala reserva = new ReservaSala();
            setField(reserva, "fecha", LocalDate.now().plusDays(i % 14));
            setField(reserva, "horaInicio", LocalTime.of(7 + (i % 9), 0));
            setField(reserva, "horaFin", LocalTime.of(8 + (i % 9), 30));
            setField(reserva, "comentarios", "Reserva de sala de prueba " + (i + 1));
            setField(reserva, "usuario", usuarios.get(i % usuarios.size()));
            setField(reserva, "sala", salas[i % salas.length]);
            setField(reserva, "serialesInvitados", new HashSet<String>());
            reservasSala.add(reserva);
        }
        reservaSalaRepository.saveAll(reservasSala);

        log.info("Datos de prueba creados: {} usuarios, {} computadores, {} reservas de computador y {} reservas de sala.",
                usuarios.size(), computadores.size(), reservasComputador.size(), reservasSala.size());
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("No fue posible inicializar el campo " + fieldName, ex);
        }
    }
}