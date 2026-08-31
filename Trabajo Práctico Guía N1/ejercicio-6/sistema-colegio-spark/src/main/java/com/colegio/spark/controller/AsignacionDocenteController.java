package com.colegio.spark.controller;

import com.colegio.spark.dto.request.AsignacionDocenteDTO;
import com.colegio.spark.service.AsignacionDocenteService;
import com.colegio.spark.service.AulaService;
import com.colegio.spark.service.DocenteService;
import com.colegio.spark.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 *  AsignacionDocenteController   (capa Controller - MVC, solo ROLE_ADMIN)
 * ============================================================================
 * Permite al administrador indicar que Docente dicta que Materia en que
 * Aula. Es el dato que despues usa NotaServiceImpl para autorizar (a nivel
 * de datos) que un docente cargue notas solo donde realmente da clases.
 * ============================================================================
 */
@Controller
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionDocenteController {

    private final AsignacionDocenteService asignacionDocenteService;
    private final DocenteService docenteService;
    private final MateriaService materiaService;
    private final AulaService aulaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asignaciones", asignacionDocenteService.listarTodas());
        return "asignaciones/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("asignacionDocenteDTO", new AsignacionDocenteDTO());
        poblarCombos(model);
        return "asignaciones/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("asignacionDocenteDTO") AsignacionDocenteDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            poblarCombos(model);
            return "asignaciones/formulario";
        }
        try {
            asignacionDocenteService.crear(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            poblarCombos(model);
            return "asignaciones/formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Asignacion creada correctamente.");
        return "redirect:/asignaciones";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        asignacionDocenteService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Asignacion eliminada correctamente.");
        return "redirect:/asignaciones";
    }

    private void poblarCombos(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        model.addAttribute("materias", materiaService.listarTodas());
        model.addAttribute("aulas", aulaService.listarTodas());
    }

}
