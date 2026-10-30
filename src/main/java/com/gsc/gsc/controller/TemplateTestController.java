package com.gsc.gsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template_test")
public class TemplateTestController {
    @GetMapping // Listens for visits to http://localhost:8080/
    public String homePage(Model model) {
        // Add data here that you want Thymeleaf to display
        // model.addAttribute("username", "Alex");

        // Returns the name of the HTML file in templates/
        return "layouts/admin-layout"; 
    }
}
