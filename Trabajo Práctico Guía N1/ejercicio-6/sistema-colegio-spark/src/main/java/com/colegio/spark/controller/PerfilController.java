package com.colegio.spark.controller;

import com.colegio.spark.dto.request.CambioPasswordDTO;
import com.colegio.spark.exception.PasswordActualIncorrectaException;
import com.colegio.spark.service.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 *  PerfilController              (capa Controller - MVC)
 * ============================================================================
 * Controlador para que CUALQUIER docente autenticado (ADMIN o DOCENTE) vea
 * sus propios datos y pueda cambiar su contraseña (requisito explicito de la
 * consigna: "el sistema debe tener la posibilidad de cambiar la contraseña").
 *
 * @AuthenticationPrincipal UserDetails
 *   Spring Security inyecta automaticamente, como parametro del metodo, los
 *   datos del usuario autenticado en la sesion actual (el mismo objeto que
 *   construye CustomUserDetailsService). Se usa userDetails.getUsername()
 *   para saber, sin confiar en ningun dato enviado por el navegador, cual es
 *   el docente que esta operando.
 * ============================================================================
 */
@Controller
@RequiredArgsConstructor
public class PerfilController {

    private final DocenteService docenteService;

    @GetMapping("/perfil")
    public String verPerfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("docente", docenteService.buscarPorEmail(userDetails.getUsername()));
        return "perfil/ver";
    }

    @GetMapping("/perfil/password")
    public String formularioCambiarPassword(Model model) {
        model.addAttribute("cambioPasswordDTO", new CambioPasswordDTO());
        return "perfil/cambiar-password";
    }

    @PostMapping("/perfil/password")
    public String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails,
                                   @Valid @ModelAttribute("cambioPasswordDTO") CambioPasswordDTO dto,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "perfil/cambiar-password";
        }
        if (!dto.getPasswordNueva().equals(dto.getConfirmarPasswordNueva())) {
            model.addAttribute("errorConfirmacion", "La nueva contraseña y su confirmacion no coinciden");
            return "perfil/cambiar-password";
        }

        try {
            docenteService.cambiarPassword(userDetails.getUsername(), dto);
        } catch (PasswordActualIncorrectaException ex) {
            model.addAttribute("errorPasswordActual", ex.getMessage());
            return "perfil/cambiar-password";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Tu contraseña se actualizo correctamente.");
        return "redirect:/perfil";
    }

}
