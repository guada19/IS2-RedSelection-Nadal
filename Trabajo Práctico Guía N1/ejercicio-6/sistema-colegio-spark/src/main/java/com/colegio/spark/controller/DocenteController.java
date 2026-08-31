package com.colegio.spark.controller;

import com.colegio.spark.dto.request.DocenteEdicionDTO;
import com.colegio.spark.service.AsignacionDocenteService;
import com.colegio.spark.service.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 *  DocenteController             (capa Controller - MVC, solo ROLE_ADMIN)
 * ============================================================================
 * A diferencia de Grado/Materia/Aula/Alumno, este Controller NO tiene un
 * "crear" (el alta de un docente es siempre auto-registro publico, ver
 * AuthController): el administrador solo puede LISTAR, VER el detalle
 * (incluyendo las materias/aulas que tiene asignadas), EDITAR sus datos
 * personales y activar/desactivar su cuenta (baja logica, ver
 * DocenteService.cambiarEstadoActivo).
 * ============================================================================
 */
@Controller
@RequestMapping("/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;
    private final AsignacionDocenteService asignacionDocenteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("docentes", docenteService.listarTodos());
        return "docentes/listar";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("docente", docenteService.buscarPorId(id));
        model.addAttribute("asignaciones", asignacionDocenteService.listarPorDocente(id));
        return "docentes/detalle";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var docente = docenteService.buscarPorId(id);
        model.addAttribute("docenteEdicionDTO", DocenteEdicionDTO.builder()
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .sexo(docente.getSexo())
                .fechaNacimiento(docente.getFechaNacimiento())
                .build());
        model.addAttribute("id", id);
        model.addAttribute("emailDocente", docente.getEmail());
        return "docentes/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("docenteEdicionDTO") DocenteEdicionDTO dto,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("emailDocente", docenteService.buscarPorId(id).getEmail());
            return "docentes/formulario";
        }
        docenteService.actualizarDatosPersonales(id, dto);
        redirectAttributes.addFlashAttribute("mensajeExito", "Datos del docente actualizados correctamente.");
        return "redirect:/docentes";
    }

    @PostMapping("/{id}/activar")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        docenteService.cambiarEstadoActivo(id, true);
        redirectAttributes.addFlashAttribute("mensajeExito", "Docente activado correctamente.");
        return "redirect:/docentes";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        docenteService.cambiarEstadoActivo(id, false);
        redirectAttributes.addFlashAttribute("mensajeExito", "Docente desactivado correctamente.");
        return "redirect:/docentes";
    }

}
