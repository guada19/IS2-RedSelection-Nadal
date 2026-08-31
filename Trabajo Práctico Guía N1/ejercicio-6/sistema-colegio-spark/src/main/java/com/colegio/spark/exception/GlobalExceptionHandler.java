package com.colegio.spark.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ============================================================================
 *  GlobalExceptionHandler        (capa Exception - manejo centralizado)
 * ============================================================================
 * Intercepta, en UN SOLO LUGAR, las excepciones que se puedan escapar de
 * cualquier Controller de la aplicacion, evitando repetir bloques try/catch
 * en cada metodo de cada Controller.
 *
 *  @ControllerAdvice
 *      Convierte esta clase en un "consejo" (advice) que aplica de forma
 *      transversal a todos los @Controller de la aplicacion.
 *
 *  @ExceptionHandler(TipoDeExcepcion.class)
 *      Cada metodo anotado asi se registra como el manejador especifico para
 *      ese tipo de excepcion (y sus subclases). Al devolver un String, Spring
 *      MVC lo interpreta como el nombre logico de la vista Thymeleaf a
 *      renderizar (siguiendo el patron MVC: el Controller/Advice decide QUE
 *      vista mostrar, sin conocer los detalles de como se renderiza el HTML).
 * ============================================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, Model model) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        model.addAttribute("mensaje", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String manejarErrorGeneral(Exception ex, Model model) {
        log.error("Error no controlado en la aplicacion", ex);
        model.addAttribute("mensaje", "Ocurrio un error inesperado. Por favor intente nuevamente.");
        return "error/500";
    }

}
