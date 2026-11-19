package com.gsc.gsc.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller 
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String rootPageRedirection(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() 
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                //log.info("User role: " + role); // Debugging line to print the user's role
                
                if (role.equals("ROLE_ADMIN") || role.equals("ROLE_MONITOR")) {
                    return "redirect:/sala/seleccionar";
                } else if (role.equals("ROLE_USER")) {
                    return "redirect:/sala/registro";
                }
            }
            
            return "redirect:/logout"; // Si el usuario tiene un rol desconocido, se cierra la sesión
        }
        
        // 3. Show public landing page to unauthenticated guests
        return "redirect:/login";
    }
    
}
