package com.empresatech.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de autenticación y acceso a la pantalla de login.
 *
 * <p>La vista de login se presenta sin requerir autenticación previa, y la
 * misma se usa tanto para el primer acceso al sistema como para mostrar
 * mensajes de error o cierre de sesión.
 */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Credenciales inválidas. Verifique usuario y contraseña.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Sesión cerrada correctamente.");
        }

        return "auth/login";
    }
}
