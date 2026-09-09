package com.club.socios.service.impl;

import com.club.socios.domain.Cuota;
import com.club.socios.domain.MedioPago;
import com.club.socios.domain.Pago;
import com.club.socios.domain.enums.EstadoCuota;
import com.club.socios.dto.PagoDTO;
import com.club.socios.exception.ReglaDeNegocioException;
import com.club.socios.mapper.PagoMapper;
import com.club.socios.repository.PagoRepository;
import com.club.socios.service.CuotaService;
import com.club.socios.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ============================================================================
 * SERVICE: Pago (implementación)
 * ============================================================================
 * Orquesta el registro de un pago:
 *   1) valida que la cuota no esté ya saldada,
 *   2) construye (vía {@link PagoMapper}) el subtipo concreto de
 *      {@link MedioPago} correspondiente (Efectivo/Transferencia/MercadoPago),
 *   3) si el medio elegido es Mercado Pago, simula la confirmación de la
 *      operación contra la pasarela de pagos,
 *   4) persiste el Pago (composición con Cuota y con MedioPago) y
 *   5) delega en {@link CuotaService} el recálculo del estado de la cuota.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final CuotaService cuotaService;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional
    public PagoDTO registrarPago(Long cuotaId, PagoDTO dto) {
        Cuota cuota = cuotaService.obtenerEntidadPorId(cuotaId);

        if (cuota.getEstado() == EstadoCuota.PAGADA) {
            throw new ReglaDeNegocioException("La cuota del período " + cuota.getPeriodo() + " ya está saldada");
        }

        BigDecimal saldoPendiente = cuota.getMontoTotal().subtract(cuota.getMontoPagado());
        if (dto.getMonto().compareTo(saldoPendiente) > 0) {
            throw new ReglaDeNegocioException(
                    "El monto del pago ($" + dto.getMonto() + ") supera el saldo pendiente ($" + saldoPendiente + ")");
        }

        // Polimorfismo: la fábrica del mapper decide, según dto.tipoMedioPago,
        // qué subclase concreta de MedioPago instanciar.
        MedioPago medioPago = pagoMapper.crearMedioPago(dto);
        simularConfirmacionSiEsMercadoPago(medioPago);

        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setFechaPago(dto.getFechaPago() != null ? dto.getFechaPago() : LocalDateTime.now());
        pago.setObservaciones(dto.getObservaciones());
        pago.setMedioPago(medioPago);

        cuota.agregarPago(pago);
        Pago pagoGuardado = pagoRepository.save(pago);

        // Recalcula PENDIENTE / PARCIAL / PAGADA / VENCIDA de la cuota afectada
        cuotaService.actualizarEstado(cuota);

        return pagoMapper.toDTO(pagoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorCuota(Long cuotaId) {
        return pagoRepository.findByCuotaIdOrderByFechaPagoDesc(cuotaId).stream()
                .map(pagoMapper::toDTO)
                .toList();
    }

    /**
     * Simula la integración con la API de Mercado Pago (checkout / webhook).
     * En un caso real, este método invocaría el SDK oficial de Mercado Pago
     * (o su API REST) y esperaría la confirmación asincrónica vía webhook;
     * aquí se resuelve de forma síncrona y simulada para fines académicos,
     * dejando el punto de extensión claramente señalado.
     */
    private void simularConfirmacionSiEsMercadoPago(MedioPago medioPago) {
        if (medioPago instanceof com.club.socios.domain.PagoMercadoPago mp) {
            if (mp.getIdTransaccionMp() == null || mp.getIdTransaccionMp().isBlank()) {
                mp.setIdTransaccionMp("MP-SIMULADO-" + System.currentTimeMillis());
            }
            if (mp.getEstadoOperacion() == null || mp.getEstadoOperacion().isBlank()) {
                mp.setEstadoOperacion("APROBADO");
            }
        }
    }
}
