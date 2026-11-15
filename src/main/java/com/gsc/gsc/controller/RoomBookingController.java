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
        
        return "views/RoomBookingGUI"; 
    }
}