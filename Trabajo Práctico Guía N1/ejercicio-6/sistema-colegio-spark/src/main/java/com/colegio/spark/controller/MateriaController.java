package com.colegio.spark.controller;

import com.colegio.spark.dto.request.MateriaDTO;
import com.colegio.spark.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD completo de Materias (capa Controller - MVC, solo ROLE_ADMIN). Ver GradoController para el detalle del patron. */
@Controller
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("materias", materiaService.listarTodas());
        return "materias/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("materiaDTO", new MateriaDTO());
        model.addAttribute("modoEdicion", false);
        return "materias/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("materiaDTO") MateriaDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicion", false);
            return "materias/formulario";
        }
        try {
            materiaService.crear(dto);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("modoEdicion", false);
            return "materias/formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia creada correctamente.");
        return "redirect:/materias";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var materia = materiaService.buscarPorId(id);
        model.addAttribute("materiaDTO",
                MateriaDTO.builder().nombre(materia.getNombre()).descripcion(materia.getDescripcion()).build());
        model.addAttribute("id", id);
        model.addAttribute("modoEdicion", true);
        return "materias/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("materiaDTO") MateriaDTO dto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("modoEdicion", true);
            return "materias/formulario";
        }
        materiaService.actualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia actualizada correctamente.");
        return "redirect:/materias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        materiaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Materia eliminada correctamente.");
        return "redirect:/materias";
    }

}
