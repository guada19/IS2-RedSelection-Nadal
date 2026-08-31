package com.colegio.spark.controller;

import com.colegio.spark.dto.request.GradoDTO;
import com.colegio.spark.service.GradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 *  GradoController               (capa Controller - MVC, solo ROLE_ADMIN)
 * ============================================================================
 * CRUD completo de Grados. Sigue siempre el mismo patron en las cinco
 * operaciones (comun a los demas controllers de administracion academica:
 * AulaController, MateriaController, AlumnoController, AsignacionController):
 *   GET  /grados            -> listar
 *   GET  /grados/nuevo      -> mostrar formulario vacio (alta)
 *   POST /grados            -> procesar alta
 *   GET  /grados/{id}/editar -> mostrar formulario precargado (edicion)
 *   POST /grados/{id}       -> procesar edicion
 *   POST /grados/{id}/eliminar -> baja fisica
 *
 * El acceso a "/grados/**" ya esta restringido a ROLE_ADMIN de forma
 * centralizada en SecurityConfig; este Controller no necesita repetir esa
 * verificacion.
 * ============================================================================
 */
@Controller
@RequestMapping("/grados")
@RequiredArgsConstructor
public class GradoController {

    private final GradoService gradoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("grados", gradoService.listarTodos());
        return "grados/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("gradoDTO", new GradoDTO());
        model.addAttribute("modoEdicion", false);
        return "grados/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("gradoDTO") GradoDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "grados/formulario";
        }
        try {
            gradoService.crear(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("modoEdicion", false);
            return "grados/formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Grado creado correctamente.");
        return "redirect:/grados";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var grado = gradoService.buscarPorId(id);
        model.addAttribute("gradoDTO", GradoDTO.builder().nombre(grado.getNombre()).nivel(grado.getNivel()).build());
        model.addAttribute("id", id);
        model.addAttribute("modoEdicion", true);
        return "grados/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("gradoDTO") GradoDTO dto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("modoEdicion", true);
            return "grados/formulario";
        }
        gradoService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Grado actualizado correctamente.");
        return "redirect:/grados";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gradoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Grado eliminado correctamente.");
        return "redirect:/grados";
    }

}
