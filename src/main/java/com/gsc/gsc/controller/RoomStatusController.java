package com.gsc.gsc.controller;

import com.gsc.gsc.dto.AsignacionPcDTO;
import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.enums.DisponibilidadComputador;
import com.gsc.gsc.service.AppUserAuthService;
import com.gsc.gsc.service.RoomStatusManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import java.util.List;

@Controller
@RequestMapping("/sala")
public class RoomStatusController {

    private final RoomStatusManager roomStatusManager;

    private final AppUserAuthService appUserAuthService;

    RoomStatusController(RoomStatusManager roomStatusManager, AppUserAuthService appUserAuthService) {
        this.roomStatusManager = roomStatusManager;
        this.appUserAuthService = appUserAuthService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model, Principal principal) {
        AppUser user = appUserAuthService.findByUsername(principal.getName());
        model.addAttribute("equiposHabilitados", roomStatusManager.obtenerEquiposDeSalaActual(user));
        return "views/UserRegistrationGUI";
    }

    @PostMapping("/registrar")
    public String procesarRegistro(@ModelAttribute AsignacionPcDTO dto, Model model, Principal principal) {
        AppUser user = appUserAuthService.findByUsername(principal.getName());
        String resultado = roomStatusManager.procesarIngresoOSalida(dto);
        model.addAttribute("resultado", resultado);
        model.addAttribute("equiposHabilitados", roomStatusManager.obtenerEquiposDeSalaActual(user));
        return "views/UserRegistrationGUI";
    }

    @GetMapping("/seleccionar")
    public String mostrarSeleccionSala(Model model) {
        model.addAttribute("salas", roomStatusManager.obtenerTodasLasSalas());
        return "views/SelectRoomGUI";
    }

    @GetMapping("/monitoreo")
    public String mostrarMonitoreoSala(@RequestParam(name = "idSala", required = false) String idSala, Model model) {
        if (idSala == null || idSala.isBlank()) {
            return "redirect:/sala/seleccionar";
        }

        List<Computador> equipos = roomStatusManager.obtenerEquiposPorSala(idSala);

        model.addAttribute("salaActual", idSala);
        model.addAttribute("computadoresSala", equipos);
        model.addAttribute("totalEquipos", equipos.size());
        model.addAttribute("totalDisponibles", roomStatusManager.contarPorEstado(equipos, DisponibilidadComputador.DISPONIBLE));
        model.addAttribute("totalOcupados", roomStatusManager.contarPorEstado(equipos, DisponibilidadComputador.OCUPADO));

        return "views/RoomMonitoringGUI";
    }
}
