package com.colegio.spark.controller;

import com.colegio.spark.dto.request.NotaDTO;
import com.colegio.spark.dto.response.AsignacionDocenteResponseDTO;
import com.colegio.spark.service.AlumnoService;
import com.colegio.spark.service.AsignacionDocenteService;
import com.colegio.spark.service.DocenteService;
import com.colegio.spark.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * ============================================================================
 *  NotaController                 (capa Controller - MVC, ROLE_ADMIN o ROLE_DOCENTE)
 * ============================================================================
 * Carga y consulta de calificaciones.
 *  - Un DOCENTE ve/edita SOLO las notas que el mismo cargo, y el formulario
 *    de alta solo le ofrece elegir entre SUS PROPIAS asignaciones (materia +
 *    aula), resueltas via AsignacionDocenteService.listarPorDocente(). La
 *    seleccion final del alumno se resuelve en la vista con javascript
 *    minimo (filtrado por aula) para simplificar la carga.
 *  - Un ADMIN puede ver el listado completo de todas las notas cargadas por
 *    todos los docentes (funcion de supervision).
 *
 * El email del docente autenticado (@AuthenticationPrincipal) es lo que
 * viaja a la capa Service para resolver "de quien son estas notas" y aplicar
 * ahi las reglas de autorizacion a nivel de datos (ver NotaServiceImpl).
 * ============================================================================
 */
@Controller
@RequestMapping("/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;
    private final AsignacionDocenteService asignacionDocenteService;
    private final DocenteService docenteService;
    private final AlumnoService alumnoService;

    @GetMapping
    public String listar(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (esAdmin(userDetails)) {
            model.addAttribute("notas", notaService.listarTodas());
        } else {
            model.addAttribute("notas", notaService.listarPorDocente(userDetails.getUsername()));
        }
        return "notas/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("notaDTO", new NotaDTO());
        poblarAsignacionesDelDocente(userDetails, model);
        return "notas/formulario";
    }

    @PostMapping
    public String crear(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @ModelAttribute("notaDTO") NotaDTO dto,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            poblarAsignacionesDelDocente(userDetails, model);
            return "notas/formulario";
        }
        try {
            notaService.crear(dto, userDetails.getUsername());
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            poblarAsignacionesDelDocente(userDetails, model);
            return "notas/formulario";
        }
        redirectAttributes.addFlashAttribute("mensajeExito", "Nota cargada correctamente.");
        return "redirect:/notas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable Long id, RedirectAttributes redirectAttributes) {
        notaService.eliminar(id, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensajeExito", "Nota eliminada correctamente.");
        return "redirect:/notas";
    }

    private boolean esAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void poblarAsignacionesDelDocente(UserDetails userDetails, Model model) {
        Long docenteId = docenteService.buscarPorEmail(userDetails.getUsername()).getId();
        List<AsignacionDocenteResponseDTO> asignaciones = asignacionDocenteService.listarPorDocente(docenteId);
        model.addAttribute("asignaciones", asignaciones);
        // Se envian TODOS los alumnos (con su aulaId) para que la vista, con un
        // pequeño script, filtre dinamicamente por aula segun la asignacion que
        // el docente elija, sin necesidad de una llamada AJAX adicional.
        model.addAttribute("alumnos", alumnoService.listarTodos());
    }

}
