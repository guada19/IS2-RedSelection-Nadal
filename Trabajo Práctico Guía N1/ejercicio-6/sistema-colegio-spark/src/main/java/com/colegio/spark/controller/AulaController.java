package com.colegio.spark.controller;

import com.colegio.spark.dto.request.AulaDTO;
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
 * CRUD completo de Aulas (capa Controller - MVC, solo ROLE_ADMIN).
 * Ademas de listar/crear/editar/eliminar, cada formulario necesita el combo
 * de Grados disponibles (poblarComboGrados), ya que Aula referencia a Grado.
 */
@Controller
@RequestMapping("/aulas")
@RequiredArgsConstructor
public class AulaController {

    private final AulaService aulaService;
    private final GradoService gradoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("aulas", aulaService.listarTodas());
        return "aulas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("aulaDTO", new AulaDTO());
        poblarComboGrados(model);
        model.addAttribute("modoEdicion", false);
        return "aulas/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("aulaDTO") AulaDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            poblarComboGrados(model);
            model.addAttribute("modoEdicion", false);
            return "aulas/formulario";
        }
        aulaService.crear(dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Aula creada correctamente.");
        return "redirect:/aulas";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var aula = aulaService.buscarPorId(id);
        model.addAttribute("aulaDTO", AulaDTO.builder()
                .nombre(aula.getNombre())
                .capacidad(aula.getCapacidad())
                .gradoId(aula.getGradoId())
                .build());
        model.addAttribute("id", id);
        poblarComboGrados(model);
        model.addAttribute("modoEdicion", true);
        return "aulas/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("aulaDTO") AulaDTO dto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            poblarComboGrados(model);
            model.addAttribute("modoEdicion", true);
            return "aulas/formulario";
        }
        aulaService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Aula actualizada correctamente.");
        return "redirect:/aulas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        aulaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Aula eliminada correctamente.");
        return "redirect:/aulas";
    }

    private void poblarComboGrados(Model model) {
        model.addAttribute("grados", gradoService.listarTodos());
    }

}
