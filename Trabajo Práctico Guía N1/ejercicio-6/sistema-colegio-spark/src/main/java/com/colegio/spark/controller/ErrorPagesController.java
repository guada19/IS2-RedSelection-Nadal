package com.colegio.spark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador minimo que expone la vista de "acceso denegado" (403),
 * referenciada desde SecurityConfig (.exceptionHandling().accessDeniedPage("/error/403")).
 * Las paginas 404 y 500 no necesitan un @GetMapping propio: las renderiza
 * directamente GlobalExceptionHandler al capturar la excepcion correspondiente.
 */
@Controller
public class ErrorPagesController {

    @GetMapping("/error/403")
    public String accesoDenegado() {
        return "error/403";
    }

}
