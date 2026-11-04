package com.gsc.gsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservas_salas")
public class RoomBookingController {

    @GetMapping
    public String bookingPage(Model model) {
        // TODO: Simular la función de extracción de datos de la base de datos SQL
        // List<Reserva> listaReservas = reservaRepository.findAll();
        // model.addAttribute("reservas", listaReservas);
        
        return "room-booking/index"; 
    }

    @PostMapping("/guardar")
    public String guardarReserva(@RequestParam("fecha") String fecha,
                                 @RequestParam("horaInicio") String horaInicio,
                                 @RequestParam("asunto") String asunto,
                                 @RequestParam("sala") String sala) {
        
        // TODO: Función temporal de guardado (Aún no interactúa con la base de datos SQL)
        System.out.println("Guardando reserva simulada para: " + fecha + " a las " + horaInicio);
        
        // Redirige de vuelta a la vista de reservas tras el envío
        return "room-booking/index";
    }
}