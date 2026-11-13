package com.gsc.gsc.controller;

import com.gsc.gsc.dto.AsignacionPcDTO;
import com.gsc.gsc.service.RoomStatusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sala")
public class RoomStatusController {

    @Autowired
    private RoomStatusManager roomStatusManager;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("todosLosEquipos", roomStatusManager.obtenerTodosLosEquipos());
        return "views/UserRegistrationGUI";
    }

    @PostMapping("/registrar")
    public String procesarRegistro(@ModelAttribute AsignacionPcDTO dto, Model model) {
        String resultado = roomStatusManager.procesarIngresoOSalida(dto);
        model.addAttribute("resultado", resultado);
        model.addAttribute("todosLosEquipos", roomStatusManager.obtenerTodosLosEquipos());
        return "views/UserRegistrationGUI";
    }

    @GetMapping("/monitoreo")
    public String mostrarMonitoreoSala(Model model) {
        model.addAttribute("computadoresSala", roomStatusManager.obtenerTodosLosEquipos());
        return "views/RoomMonitoringGUI";
    }
}
