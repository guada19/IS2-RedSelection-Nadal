package com.colegio.spark.controller;

import com.colegio.spark.dto.request.AlumnoDTO;
import com.colegio.spark.service.AlumnoService;
import com.colegio.spark.service.AulaService;
import com.colegio.spark.service.GradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CRUD completo de Alumnos (capa Controller - MVC, solo ROLE_ADMIN).
 * Cada formulario necesita los combos de Grados y Aulas disponibles, ya que
 * un Alumno referencia a ambas entidades.
 */
@Controller
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final GradoService gradoService;
    private final AulaService aulaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alumnos", alumnoService.listarTodos());
        return "alumnos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("alumnoDTO", new AlumnoDTO());
        poblarCombos(model);
        model.addAttribute("modoEdicion", false);
        return "alumnos/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("alumnoDTO") AlumnoDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            poblarCombos(model);
            model.addAttribute("modoEdicion", false);
            return "alumnos/formulario";
        }
        try {
            alumnoService.crear(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            poblarCombos(model);
            model.addAttribute("modoEdicion", false);
            return "alumnos/formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno registrado correctamente.");
        return "redirect:/alumnos";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var alumno = alumnoService.buscarPorId(id);
        model.addAttribute("alumnoDTO", AlumnoDTO.builder()
                .nombre(alumno.getNombre())
                .apellido(alumno.getApellido())
                .dni(alumno.getDni())
                .fechaNacimiento(alumno.getFechaNacimiento())
                .gradoId(alumno.getGradoId())
                .aulaId(alumno.getAulaId())
                .build());
        model.addAttribute("id", id);
        poblarCombos(model);
        model.addAttribute("modoEdicion", true);
        return "alumnos/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("alumnoDTO") AlumnoDTO dto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            poblarCombos(model);
            model.addAttribute("modoEdicion", true);
            return "alumnos/formulario";
        }
        alumnoService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno actualizado correctamente.");
        return "redirect:/alumnos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alumnoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno eliminado correctamente.");
        return "redirect:/alumnos";
    }

    private void poblarCombos(Model model) {
        model.addAttribute("grados", gradoService.listarTodos());
        model.addAttribute("aulas", aulaService.listarTodas());
    }

}
