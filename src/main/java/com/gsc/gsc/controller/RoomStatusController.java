package com.gsc.gsc.controller;

import com.gsc.gsc.dto.AsignacionPcDTO;
import com.gsc.gsc.model.Computador;
import com.gsc.gsc.model.enums.DisponibilidadComputador;
import com.gsc.gsc.service.RoomStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/sala")
public class RoomStatusController {

    @Autowired
    private RoomStatusManager roomStatusManager;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("equiposHabilitados", roomStatusManager.obtenerEquiposDeSalaActual(username));
        return "views/UserRegistrationGUI";
    }

    @PostMapping("/registrar")
    public String procesarRegistro(@ModelAttribute AsignacionPcDTO dto, Model model, Principal principal) {
        String username = principal.getName();
        String resultado = roomStatusManager.procesarIngresoOSalida(dto);
        model.addAttribute("resultado", resultado);
        model.addAttribute("equiposHabilitados", roomStatusManager.obtenerEquiposDeSalaActual(username));
        return "views/UserRegistrationGUI";
    }

    @GetMapping("/monitoreo")
    public String mostrarMonitoreoSala(Model model, Principal principal) {
        String username = principal.getName();
        List<Computador> equipos = roomStatusManager.obtenerEquiposParaMonitoreo(username);

        model.addAttribute("computadoresSala", equipos);
        model.addAttribute("totalEquipos", equipos.size());
        model.addAttribute("totalDisponibles", roomStatusManager.contarPorEstado(equipos, DisponibilidadComputador.DISPONIBLE));
        model.addAttribute("totalOcupados", roomStatusManager.contarPorEstado(equipos, DisponibilidadComputador.OCUPADO));

        return "views/RoomMonitoringGUI";
    }
}
