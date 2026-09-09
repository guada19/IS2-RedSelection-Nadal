package com.club.socios.controller;

import com.club.socios.repository.CuotaRepository;
import com.club.socios.repository.RegistroAccesoRepository;
import com.club.socios.repository.SocioRepository;
import com.club.socios.domain.enums.EstadoCuota;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SocioRepository socioRepository;
    private final CuotaRepository cuotaRepository;
    private final RegistroAccesoRepository registroAccesoRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalSocios", socioRepository.findByActivoTrue().size());
        model.addAttribute("cuotasPendientes", cuotaRepository.findByEstado(EstadoCuota.PENDIENTE).size());
        model.addAttribute("cuotasVencidas", cuotaRepository.findByEstado(EstadoCuota.VENCIDA).size());
        model.addAttribute("accesosHoy", registroAccesoRepository
                .findByFechaHoraBetweenOrderByFechaHoraDesc(
                        LocalDateTime.now().toLocalDate().atStartOfDay(), LocalDateTime.now())
                .size());
        return "dashboard";
    }
}
