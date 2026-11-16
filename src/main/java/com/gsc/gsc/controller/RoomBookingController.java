package com.gsc.gsc.controller;

import com.gsc.gsc.dto.RoomBookingDto;
import com.gsc.gsc.service.RoomBookingService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas_salas")
public class RoomBookingController {

    private final RoomBookingService roomBookingService;

    // Spring inyecta automáticamente tu RoomBookingService aquí
    public RoomBookingController(RoomBookingService roomBookingService) {
        this.roomBookingService = roomBookingService;
    }

    // 1. Muestra la pantalla con un DTO vacío listo para llenar
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("reservaDto", new RoomBookingDto());
        return "views/RoomBookingGUI";
    }

    // 2. Recibe el formulario al pulsar "Crear reserva" y llama al servicio
    @PostMapping
    public String crearReserva(@ModelAttribute("reservaDto") RoomBookingDto reservaDto,
                               RedirectAttributes redirectAttributes) {
        try {
            roomBookingService.registrarReserva(reservaDto);
            redirectAttributes.addFlashAttribute("mensajeExito", "Reserva registrada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Atrapa tus mensajes personalizados (horario, sobrelapamiento, campos vacíos)
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // Atrapa errores si el ID de sala o usuario no existe en PostgreSQL
            redirectAttributes.addFlashAttribute("mensajeError", "El ID de la sala o del usuario no existe en la base de datos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error inesperado al guardar la reserva.");
        }

        return "redirect:/reservas_salas";
    }
}