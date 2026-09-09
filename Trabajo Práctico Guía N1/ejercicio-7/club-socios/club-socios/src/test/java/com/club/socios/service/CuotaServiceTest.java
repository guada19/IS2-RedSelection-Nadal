package com.club.socios.service;

import com.club.socios.domain.Cuota;
import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.domain.enums.EstadoCuota;
import com.club.socios.dto.CuotaDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.mapper.CuotaMapper;
import com.club.socios.repository.CuotaRepository;
import com.club.socios.repository.GrupoFamiliarRepository;
import com.club.socios.service.impl.CuotaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CuotaService - pruebas unitarias")
class CuotaServiceTest {

    @Mock
    private CuotaRepository cuotaRepository;
    @Mock
    private GrupoFamiliarRepository grupoFamiliarRepository;
    @Mock
    private CuotaMapper cuotaMapper;

    @InjectMocks
    private CuotaServiceImpl cuotaService;

    @Test
    @DisplayName("generar() debe rechazar una cuota duplicada para el mismo grupo y período")
    void generar_cuotaDuplicada_lanzaExcepcion() {
        GrupoFamiliar grupo = new GrupoFamiliar();
        grupo.setId(1L);
        when(grupoFamiliarRepository.findById(1L)).thenReturn(Optional.of(grupo));
        when(cuotaRepository.findByGrupoFamiliarIdAndPeriodo(1L, "2026-09"))
                .thenReturn(Optional.of(new Cuota()));

        CuotaDTO dto = CuotaDTO.builder()
                .grupoFamiliarId(1L)
                .periodo("2026-09")
                .montoTotal(new BigDecimal("20000"))
                .build();

        assertThatThrownBy(() -> cuotaService.generar(dto))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("2026-09");
    }

    @Test
    @DisplayName("actualizarEstado() debe marcar PAGADA cuando lo pagado cubre el monto total")
    void actualizarEstado_montoCompleto_marcaPagada() {
        Cuota cuota = cuotaConMontoYPagos(new BigDecimal("10000"), new BigDecimal("10000"));

        cuotaService.actualizarEstado(cuota);

        assertThat(cuota.getEstado()).isEqualTo(EstadoCuota.PAGADA);
    }

    @Test
    @DisplayName("actualizarEstado() debe marcar PARCIAL cuando lo pagado es menor al total pero mayor a cero")
    void actualizarEstado_pagoParcial_marcaParcial() {
        Cuota cuota = cuotaConMontoYPagos(new BigDecimal("10000"), new BigDecimal("4000"));

        cuotaService.actualizarEstado(cuota);

        assertThat(cuota.getEstado()).isEqualTo(EstadoCuota.PARCIAL);
    }

    @Test
    @DisplayName("actualizarEstado() debe marcar VENCIDA cuando no hay pagos y ya venció el plazo")
    void actualizarEstado_sinPagosYVencida_marcaVencida() {
        Cuota cuota = cuotaConMontoYPagos(new BigDecimal("10000"), BigDecimal.ZERO);
        cuota.setFechaVencimiento(LocalDate.now().minusDays(1));

        cuotaService.actualizarEstado(cuota);

        assertThat(cuota.getEstado()).isEqualTo(EstadoCuota.VENCIDA);
    }

    /** Helper: arma una Cuota con un monto total y una suma de pagos ya cargada, sin persistencia real. */
    private Cuota cuotaConMontoYPagos(BigDecimal montoTotal, BigDecimal totalPagado) {
        Cuota cuota = new Cuota();
        cuota.setMontoTotal(montoTotal);
        cuota.setFechaVencimiento(LocalDate.now().plusDays(10));

        if (totalPagado.compareTo(BigDecimal.ZERO) > 0) {
            com.club.socios.domain.Pago pago = new com.club.socios.domain.Pago();
            pago.setMonto(totalPagado);
            cuota.getPagos().add(pago);
        }
        when(cuotaRepository.save(any(Cuota.class))).thenReturn(cuota);
        return cuota;
    }
}
