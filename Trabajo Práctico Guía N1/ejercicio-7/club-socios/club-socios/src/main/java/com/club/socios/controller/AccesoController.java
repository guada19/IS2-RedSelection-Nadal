package com.club.socios.controller;

import com.club.socios.dto.RegistroAccesoDTO;
import com.club.socios.exception.RecursoNoEncontradoException;
import com.club.socios.repository.FamiliarSocioRepository;
import com.club.socios.service.RegistroAccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * CONTROLLER: control de acceso de un FAMILIAR
 * ============================================================================
 * El registro de acceso del Socio titular vive en {@code SocioController}
 * por comodidad de navegación, pero ambos delegan en el mismo
 * {@link RegistroAccesoService}, ya que Socio y FamiliarSocio son subtipos
 * de la misma superclase Persona (HERENCIA).
 *
 * Nota de diseño: para resolver a qué ficha de socio redirigir después de
 * registrar el acceso de un familiar, se usa una consulta de proyección
 * dedicada ({@code FamiliarSocioRepository.buscarIdSocioTitular}) en lugar
 * de navegar las asociaciones lazy de la entidad desde el Controller, ya
 * que la aplicación corre con {@code spring.jpa.open-in-view=false} (no
 * hay sesión de Hibernate abierta fuera de la capa de Service/Repository).
 * ============================================================================
 */
@Controller
@RequestMapping("/familiares")
@RequiredArgsConstructor
public class AccesoController {

    private final RegistroAccesoService registroAccesoService;
    private final FamiliarSocioRepository familiarSocioRepository;

    @PostMapping("/{id}/acceso")
    public String registrarAcceso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long socioTitularId = familiarSocioRepository.buscarIdSocioTitular(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Familiar no encontrado: " + id));

        RegistroAccesoDTO registro = registroAccesoService.registrarMovimiento(id, "Molinete Principal", true);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Movimiento registrado para " + registro.getPersonaNombreCompleto() + ": " + registro.getTipo());

        return "redirect:/socios/" + socioTitularId;
    }
}
