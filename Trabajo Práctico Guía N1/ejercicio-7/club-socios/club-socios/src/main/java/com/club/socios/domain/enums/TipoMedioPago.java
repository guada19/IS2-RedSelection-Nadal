package com.club.socios.domain.enums;

/**
 * Discriminador de la jerarquía de herencia {@code MedioPago}.
 * Se usa con la estrategia SINGLE_TABLE (ver clase MedioPago).
 */
public enum TipoMedioPago {
    EFECTIVO,
    TRANSFERENCIA,
    MERCADO_PAGO
}
