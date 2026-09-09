package com.club.socios.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * MANEJADOR GLOBAL DE EXCEPCIONES (capa MVC)
 * ============================================================================
 * Centraliza la traducción de excepciones de negocio en mensajes legibles
 * para el usuario, en vez de repetir try/catch en cada método de cada
 * controlador. Se aplica a TODOS los @Controller de la aplicación.
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarNoEncontrado(RecursoNoEncontradoException ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error/404-negocio";
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public String manejarReglaNegocio(ReglaDeNegocioException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        return "redirect:/socios";
    }
}
