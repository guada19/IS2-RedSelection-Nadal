package com.club.socios.controller;

import com.club.socios.dto.PagoDTO;
import com.club.socios.service.CuotaService;
import com.club.socios.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * CONTROLLER: Pago
 * ============================================================================
 * Registra el pago (total o parcial) de una {@code Cuota}, con cualquiera
 * de los 3 medios de pago soportados. El formulario Thymeleaf muestra/oculta
 * dinámicamente (con JS liviano) los campos propios de cada medio de pago
 * (ver fragmento "cuotas/detalle.html").
 * ============================================================================
 */
@Controller
@RequestMapping("/cuotas/{cuotaId}/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final CuotaService cuotaService;

    @PostMapping
    public String registrarPago(@PathVariable Long cuotaId,
                                 @Valid @ModelAttribute("nuevoPago") PagoDTO pago,
                                 BindingResult resultado,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("cuota", cuotaService.obtenerPorId(cuotaId));
            return "cuotas/detalle";
        }
        PagoDTO registrado = pagoService.registrarPago(cuotaId, pago);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Pago de $" + registrado.getMonto() + " registrado (" + registrado.getDescripcionMedioPago() + ")");
        return "redirect:/cuotas/" + cuotaId;
    }
}
