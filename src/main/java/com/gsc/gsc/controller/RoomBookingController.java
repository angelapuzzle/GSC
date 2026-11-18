package com.gsc.gsc.controller;

import com.gsc.gsc.dto.RoomBookingDto;
import com.gsc.gsc.service.RoomBookingService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sala/reservas")
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
        
        return "redirect:/sala/reservas";
    }
    
    // READ: Devuelve el RoomBookingDto en JSON para llenar el modal
    @GetMapping("/buscar/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarReserva(@PathVariable("id") Long id) {
        try {
            RoomBookingDto filtro = new RoomBookingDto();
            filtro.setIdReserva(id);
            RoomBookingDto resultado = roomBookingService.buscarReserva(filtro);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado al consultar la reserva.");
        }
    }

    // UPDATE: Recibe el RoomBookingDto completo (con idReserva desde el input readonly)
    @PostMapping("/editar")
    public String editarReserva(@ModelAttribute RoomBookingDto reservaDto,
                                RedirectAttributes redirectAttributes) {
        try {
            roomBookingService.actualizarReserva(reservaDto);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Reserva #" + reservaDto.getIdReserva() + " actualizada correctamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "El ID de la sala o del usuario no existe en la base de datos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ocurrió un error inesperado al actualizar la reserva.");
        }
        return "redirect:/sala/reservas";
    }

    // DELETE: Recibe únicamente el idReserva desde el input oculto
    @PostMapping("/eliminar")
    public String eliminarReserva(@RequestParam("idReserva") Long idReserva,
                                  RedirectAttributes redirectAttributes) {
        try {
            roomBookingService.eliminarReserva(idReserva);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Reserva #" + idReserva + " eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Ocurrió un error inesperado al eliminar la reserva.");
        }
        return "redirect:/sala/reservas";
    }
}