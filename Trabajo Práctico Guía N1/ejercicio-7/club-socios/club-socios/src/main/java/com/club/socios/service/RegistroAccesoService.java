package com.club.socios.service;

import com.club.socios.dto.RegistroAccesoDTO;

import java.util.List;

public interface RegistroAccesoService {

    /**
     * Registra el paso de una persona por el control de acceso. El TIPO
     * (ENTRADA/SALIDA) se infiere automáticamente en base a su último
     * movimiento registrado (si el último fue ENTRADA, este es SALIDA, y
     * viceversa) tal como describe el enunciado base del ejercicio.
     */
    RegistroAccesoDTO registrarMovimiento(Long personaId, String puntoAcceso, boolean reconocimientoExitoso);

    List<RegistroAccesoDTO> historialDePersona(Long personaId);
}
