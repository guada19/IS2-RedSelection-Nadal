package com.empresatech.app.model;

/**
 * Enum que representa los posibles estados por los que puede pasar una
 * Factura (tanto de Cliente como de Proveedor) durante su ciclo de vida.
 *
 * Se mapea en la entidad Factura mediante @Enumerated(EnumType.STRING),
 * lo que hace que se guarde en la base de datos como texto legible
 * ("PENDIENTE", "PAGADA", etc.) en lugar del índice numérico del enum,
 * evitando errores si en el futuro se reordenan o agregan valores.
 */
public enum EstadoFactura {
    PENDIENTE,
    PAGADA,
    ANULADA
}
