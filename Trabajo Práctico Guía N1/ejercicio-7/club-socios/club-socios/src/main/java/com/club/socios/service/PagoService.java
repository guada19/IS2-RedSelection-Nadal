package com.club.socios.service;

import com.club.socios.dto.PagoDTO;

import java.util.List;

public interface PagoService {

    /**
     * Registra un pago (total o parcial) de una cuota, con el medio de
     * pago indicado en el DTO (Efectivo, Transferencia o Mercado Pago).
     * Luego de guardar el pago, recalcula el estado de la cuota.
     */
    PagoDTO registrarPago(Long cuotaId, PagoDTO dto);

    List<PagoDTO> listarPorCuota(Long cuotaId);
}
