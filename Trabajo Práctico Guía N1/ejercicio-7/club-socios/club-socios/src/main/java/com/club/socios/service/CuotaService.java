package com.club.socios.service;

import com.club.socios.domain.Cuota;
import com.club.socios.dto.CuotaDTO;

import java.util.List;

public interface CuotaService {

    /** Genera la cuota de un período para un grupo familiar. */
    CuotaDTO generar(CuotaDTO dto);

    List<CuotaDTO> listarPorGrupo(Long grupoFamiliarId);

    CuotaDTO obtenerPorId(Long id);

    Cuota obtenerEntidadPorId(Long id);

    /** Recalcula el estado (PENDIENTE/PARCIAL/PAGADA/VENCIDA) según lo pagado hasta el momento. */
    void actualizarEstado(Cuota cuota);
}
