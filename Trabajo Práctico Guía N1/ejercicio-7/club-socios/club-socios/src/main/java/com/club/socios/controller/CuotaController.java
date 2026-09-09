package com.club.socios.controller;

import com.club.socios.dto.CuotaDTO;
import com.club.socios.service.CuotaService;
import com.club.socios.service.GrupoFamiliarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * CONTROLLER: Cuota
 * ============================================================================
 * Expone la nueva funcionalidad pedida en el enunciado: generar y consultar
 * la cuota mensual de cada grupo familiar. El registro del PAGO en sí
 * (con su medio de pago) se maneja en {@link PagoController}, separando
 * responsabilidades: "generar el cargo" vs. "saldar el cargo".
 * ============================================================================
 */
@Controller
@RequestMapping("/cuotas")
@RequiredArgsConstructor
public class CuotaController {

    private final CuotaService cuotaService;
    private final GrupoFamiliarService grupoFamiliarService;

    @GetMapping("/nueva")
    public String formularioNueva(@RequestParam Long grupoFamiliarId, Model model) {
        model.addAttribute("cuota", CuotaDTO.builder().grupoFamiliarId(grupoFamiliarId).build());
        model.addAttribute("grupoFamiliar", grupoFamiliarService.obtenerPorId(grupoFamiliarId));
        return "cuotas/formulario";
    }

    @PostMapping
    public String generar(@Valid @ModelAttribute("cuota") CuotaDTO cuota,
                           BindingResult resultado,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("grupoFamiliar", grupoFamiliarService.obtenerPorId(cuota.getGrupoFamiliarId()));
            return "cuotas/formulario";
        }
        CuotaDTO generada = cuotaService.generar(cuota);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Cuota del período " + generada.getPeriodo() + " generada correctamente");
        return "redirect:/cuotas/" + generada.getId();
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        CuotaDTO cuota = cuotaService.obtenerPorId(id);
        model.addAttribute("cuota", cuota);
        model.addAttribute("nuevoPago", com.club.socios.dto.PagoDTO.builder().cuotaId(id).build());
        return "cuotas/detalle";
    }
}
