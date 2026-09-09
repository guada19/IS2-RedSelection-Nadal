package com.club.socios.service;

import com.club.socios.domain.Cuota;
import com.club.socios.domain.MedioPago;
import com.club.socios.domain.Pago;
import com.club.socios.domain.PagoEfectivo;
import com.club.socios.domain.enums.EstadoCuota;
import com.club.socios.domain.enums.TipoMedioPago;
import com.club.socios.dto.PagoDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.mapper.PagoMapper;
import com.club.socios.repository.PagoRepository;
import com.club.socios.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ============================================================================
 * PRUEBAS UNITARIAS - PagoService
 * ============================================================================
 * Casos clave a cubrir según las reglas de negocio del ejercicio:
 *  1) no se puede pagar una cuota ya saldada,
 *  2) no se puede pagar un monto mayor al saldo pendiente,
 *  3) el pago se guarda con el subtipo de MedioPago correcto (polimorfismo).
 * ============================================================================
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - pruebas unitarias")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private CuotaService cuotaService;
    @Mock
    private PagoMapper pagoMapper;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @Test
    @DisplayName("registrarPago() debe rechazar el pago si la cuota ya está PAGADA")
    void registrarPago_cuotaYaPagada_lanzaExcepcion() {
        Cuota cuota = new Cuota();
        cuota.setEstado(EstadoCuota.PAGADA);
        cuota.setMontoTotal(new BigDecimal("10000"));
        cuota.setPeriodo("2026-09");
        when(cuotaService.obtenerEntidadPorId(1L)).thenReturn(cuota);

        PagoDTO dto = PagoDTO.builder().monto(new BigDecimal("1000")).tipoMedioPago(TipoMedioPago.EFECTIVO).build();

        assertThatThrownBy(() -> pagoService.registrarPago(1L, dto))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("saldada");

        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("registrarPago() debe rechazar un monto mayor al saldo pendiente")
    void registrarPago_montoSuperaSaldo_lanzaExcepcion() {
        Cuota cuota = new Cuota();
        cuota.setEstado(EstadoCuota.PENDIENTE);
        cuota.setMontoTotal(new BigDecimal("10000"));
        when(cuotaService.obtenerEntidadPorId(1L)).thenReturn(cuota);

        PagoDTO dto = PagoDTO.builder().monto(new BigDecimal("15000")).tipoMedioPago(TipoMedioPago.EFECTIVO).build();

        assertThatThrownBy(() -> pagoService.registrarPago(1L, dto))
                .isInstanceOf(ReglaDeNegocioException.class)
                .hasMessageContaining("saldo pendiente");
    }

    @Test
    @DisplayName("registrarPago() con datos válidos guarda el pago y recalcula el estado de la cuota")
    void registrarPago_datosValidos_guardaYRecalcula() {
        Cuota cuota = new Cuota();
        cuota.setEstado(EstadoCuota.PENDIENTE);
        cuota.setMontoTotal(new BigDecimal("10000"));
        when(cuotaService.obtenerEntidadPorId(1L)).thenReturn(cuota);

        PagoDTO dto = PagoDTO.builder()
                .monto(new BigDecimal("10000"))
                .tipoMedioPago(TipoMedioPago.EFECTIVO)
                .numeroRecibo("R-001")
                .build();

        MedioPago medioPago = new PagoEfectivo();
        when(pagoMapper.crearMedioPago(dto)).thenReturn(medioPago);
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pagoMapper.toDTO(any(Pago.class))).thenReturn(dto);

        pagoService.registrarPago(1L, dto);

        verify(pagoRepository).save(any(Pago.class));
        verify(cuotaService).actualizarEstado(cuota);
    }
}
