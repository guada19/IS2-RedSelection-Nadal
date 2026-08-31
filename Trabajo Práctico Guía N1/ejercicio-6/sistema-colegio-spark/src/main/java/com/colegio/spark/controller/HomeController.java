package com.colegio.spark.controller;

import com.colegio.spark.repository.AlumnoRepository;
import com.colegio.spark.repository.DocenteRepository;
import com.colegio.spark.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ============================================================================
 *  HomeController                (capa Controller - MVC)
 * ============================================================================
 * Controlador de la pagina de inicio, posterior al login (/panel). A pedido
 * de la consigna, NO reproduce el dashboard con graficos de la plantilla
 * original (esa parte se descarta); en su lugar arma un panel simple de
 * bienvenida con algunos totales generales, reutilizando el layout general
 * (sidebar/topbar) de la plantilla.
 * ============================================================================
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    @GetMapping("/")
    public String raiz() {
        return "redirect:/panel";
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        model.addAttribute("totalAlumnos", alumnoRepository.count());
        model.addAttribute("totalDocentes", docenteRepository.count());
        model.addAttribute("totalMaterias", materiaRepository.count());
        return "panel";
    }

}
