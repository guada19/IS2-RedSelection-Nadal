package com.club.socios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de autenticación. La lógica de validar usuario/contraseña la
 * resuelve Spring Security (ver {@code SecurityConfig}); este controlador
 * sólo es responsable de RENDERIZAR la vista de login (rol puro de "View"
 * dentro del patrón MVC).
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/dashboard";
    }
}
