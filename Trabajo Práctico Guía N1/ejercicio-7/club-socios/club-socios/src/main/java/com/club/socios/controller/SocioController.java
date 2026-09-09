package com.club.socios.controller;

import com.club.socios.dto.CuotaDTO;
import com.club.socios.dto.GrupoFamiliarDTO;
import com.club.socios.dto.RegistroAccesoDTO;
import com.club.socios.dto.SocioDTO;
import com.club.socios.service.CuotaService;
import com.club.socios.service.GrupoFamiliarService;
import com.club.socios.service.RegistroAccesoService;
import com.club.socios.service.SocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * CONTROLLER (capa MVC - "C"): Socio
 * ============================================================================
 * Responsabilidades del controlador (y SOLO estas):
 *   1) recibir la petición HTTP y sus parámetros/DTOs,
 *   2) delegar toda la lógica de negocio al Service correspondiente,
 *   3) elegir qué plantilla Thymeleaf renderizar (la "Vista").
 * No contiene lógica de negocio ni accede a Repositories directamente.
 * ============================================================================
 */
@Controller
@RequestMapping("/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;
    private final GrupoFamiliarService grupoFamiliarService;
    private final CuotaService cuotaService;
    private final RegistroAccesoService registroAccesoService;

    @GetMapping
    public String listar(@RequestParam(required = false) String texto, Model model) {
        model.addAttribute("socios", socioService.buscar(texto));
        model.addAttribute("textoBusqueda", texto);
        return "socios/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("socio", SocioDTO.builder().build());
        model.addAttribute("esNuevo", true);
        return "socios/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("socio") SocioDTO socio,
                         BindingResult resultado,
                         @RequestParam(required = false) MultipartFile fotoRostro,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("esNuevo", true);
            return "socios/formulario";
        }
        SocioDTO creado = socioService.crear(socio, fotoRostro);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Socio " + creado.getNombre() + " " + creado.getApellido() + " registrado correctamente");
        return "redirect:/socios/" + creado.getId();
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        SocioDTO socio = socioService.obtenerPorId(id);
        GrupoFamiliarDTO grupoFamiliar = grupoFamiliarService.obtenerPorSocio(id);
        java.util.List<CuotaDTO> cuotas = cuotaService.listarPorGrupo(grupoFamiliar.getId());
        java.util.List<RegistroAccesoDTO> historial = registroAccesoService.historialDePersona(id);

        model.addAttribute("socio", socio);
        model.addAttribute("grupoFamiliar", grupoFamiliar);
        model.addAttribute("cuotas", cuotas);
        model.addAttribute("historialAccesos", historial);
        model.addAttribute("nuevoFamiliar", com.club.socios.dto.FamiliarSocioDTO.builder().build());
        return "socios/detalle";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("socio", socioService.obtenerPorId(id));
        model.addAttribute("esNuevo", false);
        return "socios/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("socio") SocioDTO socio,
                              BindingResult resultado,
                              @RequestParam(required = false) MultipartFile fotoRostro,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            // El formulario no envía el id (sólo va en la URL): se restituye
            // al DTO para que la vista pueda seguir armando correctamente
            // la URL de acción del formulario si el usuario reintenta guardar.
            socio.setId(id);
            model.addAttribute("esNuevo", false);
            return "socios/formulario";
        }
        socioService.actualizar(id, socio, fotoRostro);
        redirectAttributes.addFlashAttribute("mensajeExito", "Datos del socio actualizados correctamente");
        return "redirect:/socios/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        socioService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Socio dado de baja correctamente");
        return "redirect:/socios";
    }

    // -------------------------------------------------------------------
    // Gestión del grupo familiar (AGREGACIÓN) desde la ficha del socio
    // -------------------------------------------------------------------

    @PostMapping("/{id}/familiares")
    public String agregarFamiliar(@PathVariable Long id,
                                   @ModelAttribute("nuevoFamiliar") com.club.socios.dto.FamiliarSocioDTO familiar,
                                   @RequestParam(required = false) MultipartFile fotoRostro,
                                   RedirectAttributes redirectAttributes) {
        grupoFamiliarService.agregarFamiliar(id, familiar, fotoRostro);
        redirectAttributes.addFlashAttribute("mensajeExito", "Familiar agregado al grupo correctamente");
        return "redirect:/socios/" + id;
    }

    @PostMapping("/{id}/familiares/{familiarId}/eliminar")
    public String quitarFamiliar(@PathVariable Long id,
                                  @PathVariable Long familiarId,
                                  RedirectAttributes redirectAttributes) {
        GrupoFamiliarDTO grupo = grupoFamiliarService.obtenerPorSocio(id);
        grupoFamiliarService.quitarFamiliar(grupo.getId(), familiarId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Familiar desvinculado del grupo");
        return "redirect:/socios/" + id;
    }

    // -------------------------------------------------------------------
    // Control de acceso (entrada/salida) desde la ficha del socio/familiar
    // -------------------------------------------------------------------

    @PostMapping("/{id}/acceso")
    public String registrarAcceso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        RegistroAccesoDTO registro = registroAccesoService.registrarMovimiento(id, "Molinete Principal", true);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Movimiento registrado: " + registro.getTipo() + " a las " + registro.getFechaHora());
        return "redirect:/socios/" + id;
    }
}
