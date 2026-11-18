package com.gsc.gsc.controller;

import com.gsc.gsc.dto.MonitorAuthDto;
import com.gsc.gsc.model.AppUser;
import com.gsc.gsc.service.MonitorAuthenticator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor")
public class MonitorAuthenticatorController {

    private final MonitorAuthenticator monitorAuthenticator;

    public MonitorAuthenticatorController(MonitorAuthenticator monitorAuthenticator) {
        this.monitorAuthenticator = monitorAuthenticator;
    }

    @GetMapping("/autenticar")
    public String mostrarFormularioAutenticacion(Model model) {
        model.addAttribute("authDto", new MonitorAuthDto());
        return "views/MonitorAuthenticatorGUI";
    }

    @PostMapping("/autenticar")
    public String procesarAutenticacion(@ModelAttribute("authDto") MonitorAuthDto authDto,
                                        HttpSession session,
                                        Model model) {
        try {
            AppUser monitor = monitorAuthenticator.autenticarMonitorConDocumento(authDto);
            session.setAttribute("monitorAutenticado", monitor.getUsername());
            session.setAttribute("monitorRol", monitor.getRole());

            if (authDto.getDocumento() != null && !authDto.getDocumento().isBlank()) {
                session.setAttribute("monitorDocumento", authDto.getDocumento().trim());
            }

            model.addAttribute("resultado", "exito");
            model.addAttribute("monitorNombre", monitor.getUsername());
            model.addAttribute("monitorRol", monitor.getRole());
        } catch (IllegalArgumentException e) {
            model.addAttribute("resultado", "error");
            model.addAttribute("mensajeError", e.getMessage());
        }

        return "views/MonitorAuthenticatorGUI";
    }
}
